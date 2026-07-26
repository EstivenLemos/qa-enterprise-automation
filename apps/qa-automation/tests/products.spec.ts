import type { APIRequestContext } from '@playwright/test';
import { test, expect } from '../support/fixtures';
import { loginAsAdmin, authHeader } from '../support/api-client';
import { uniqueName } from '../support/test-data';

async function createCategory(request: APIRequestContext, adminToken: string): Promise<number> {

	const response = await request.post('categories', {
		data: { name: uniqueName('Category') },
		headers: authHeader(adminToken)
	});
	const body = await response.json();

	return body.data.id;

}

test.describe('Products API', () => {

	test('GET /products: es público y devuelve un ApiResponse con lista', async ({ request }) => {

		const response = await request.get('products');
		const body = await response.json();

		expect(response.status()).toBe(200);
		expect(body.success).toBe(true);
		expect(Array.isArray(body.data)).toBe(true);

	});

	test('GET /products/{id}: 404 cuando no existe', async ({ request }) => {

		const response = await request.get('products/999999');

		expect(response.status()).toBe(404);

	});

	test('POST /products: categoría inexistente devuelve 404', async ({ request }) => {

		const adminToken = await loginAsAdmin(request);

		const response = await request.post('products', {
			data: { name: 'Consola', description: 'desc', price: 199.99, stock: 5, categoryId: 999999 },
			headers: authHeader(adminToken)
		});

		expect(response.status()).toBe(404);

	});

	test('POST /products: precio negativo devuelve 400', async ({ request }) => {

		const adminToken = await loginAsAdmin(request);
		const categoryId = await createCategory(request, adminToken);

		const response = await request.post('products', {
			data: { name: 'Consola', description: 'desc', price: -10, stock: 5, categoryId },
			headers: authHeader(adminToken)
		});
		const body = await response.json();

		expect(response.status()).toBe(400);
		expect(body.success).toBe(false);

	});

	test('POST /products: sin token devuelve 401/403', async ({ request }) => {

		const adminToken = await loginAsAdmin(request);
		const categoryId = await createCategory(request, adminToken);

		const response = await request.post('products', {
			data: { name: 'Consola', description: 'desc', price: 100, stock: 5, categoryId }
		});

		expect([401, 403]).toContain(response.status());

	});

	test('POST /products: ADMIN crea el producto con la categoría resuelta por nombre', async ({ request }) => {

		const adminToken = await loginAsAdmin(request);
		const categoryName = uniqueName('Gaming');

		const categoryResponse = await request.post('categories', {
			data: { name: categoryName },
			headers: authHeader(adminToken)
		});
		const categoryBody = await categoryResponse.json();

		const productName = uniqueName('Console');

		const createResponse = await request.post('products', {
			data: {
				name: productName,
				description: 'Next-gen console',
				price: 499.99,
				stock: 10,
				categoryId: categoryBody.data.id
			},
			headers: authHeader(adminToken)
		});
		const createBody = await createResponse.json();

		expect(createResponse.status()).toBe(201);
		expect(createBody.data.name).toBe(productName);
		expect(createBody.data.category).toBe(categoryName);

		const getResponse = await request.get(`products/${createBody.data.id}`);
		const getBody = await getResponse.json();

		expect(getResponse.status()).toBe(200);
		expect(getBody.data.name).toBe(productName);

	});

});
