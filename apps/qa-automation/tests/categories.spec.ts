import { test, expect } from '../support/fixtures';
import { register, loginAsAdmin, authHeader } from '../support/api-client';
import { uniqueEmail, uniqueName, randomPassword } from '../support/test-data';

test.describe('Categories API', () => {

	test('GET /categories: es público y devuelve un ApiResponse con lista', async ({ request }) => {

		const response = await request.get('categories');
		const body = await response.json();

		expect(response.status()).toBe(200);
		expect(body.success).toBe(true);
		expect(Array.isArray(body.data)).toBe(true);

	});

	test('GET /categories/{id}: 404 cuando no existe', async ({ request }) => {

		const response = await request.get('categories/999999');
		const body = await response.json();

		expect(response.status()).toBe(404);
		expect(body.success).toBe(false);

	});

	test('POST /categories: sin token devuelve 401/403', async ({ request }) => {

		const response = await request.post('categories', { data: { name: uniqueName('Cat') } });

		expect([401, 403]).toContain(response.status());

	});

	test('POST /categories: con rol USER devuelve 403', async ({ request }) => {

		const email = uniqueEmail('user-cat');
		const password = randomPassword();
		const { body: authBody } = await register(request, { firstName: 'User', lastName: 'QA', email, password });

		const response = await request.post('categories', {
			data: { name: uniqueName('Cat') },
			headers: authHeader(authBody.data.accessToken)
		});

		expect(response.status()).toBe(403);

	});

	test('POST /categories: nombre en blanco devuelve 400', async ({ request }) => {

		const adminToken = await loginAsAdmin(request);

		const response = await request.post('categories', {
			data: { name: '   ' },
			headers: authHeader(adminToken)
		});
		const body = await response.json();

		expect(response.status()).toBe(400);
		expect(body.success).toBe(false);

	});

	test('POST /categories: ADMIN crea la categoría y luego se puede consultar por id', async ({ request }) => {

		const adminToken = await loginAsAdmin(request);
		const name = uniqueName('Category');

		const createResponse = await request.post('categories', {
			data: { name },
			headers: authHeader(adminToken)
		});
		const createBody = await createResponse.json();

		expect(createResponse.status()).toBe(201);
		expect(createBody.data.name).toBe(name);

		const getResponse = await request.get(`categories/${createBody.data.id}`);
		const getBody = await getResponse.json();

		expect(getResponse.status()).toBe(200);
		expect(getBody.data.name).toBe(name);
		expect(getBody.data.createdAt).toBeTruthy();

	});

});
