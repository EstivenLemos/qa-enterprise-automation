import { test, expect } from '../support/fixtures';
import { register, login } from '../support/api-client';
import { uniqueEmail, randomPassword } from '../support/test-data';

test.describe('Auth API', () => {

	test('register: crea un usuario nuevo y devuelve tokens', async ({ request }) => {

		const email = uniqueEmail('register');

		const { status, body } = await register(request, {
			firstName: 'Ana',
			lastName: 'QA',
			email,
			password: randomPassword()
		});

		expect(status).toBe(201);
		expect(body.success).toBe(true);
		expect(body.data.accessToken).toBeTruthy();
		expect(body.data.refreshToken).toBeTruthy();

	});

	test('register: email duplicado devuelve 409', async ({ request }) => {

		const email = uniqueEmail('duplicate');
		const password = randomPassword();

		await register(request, { firstName: 'Ana', lastName: 'QA', email, password });
		const { status, body } = await register(request, { firstName: 'Ana', lastName: 'QA', email, password });

		expect(status).toBe(409);
		expect(body.success).toBe(false);

	});

	test('login: credenciales válidas devuelve 200 con tokens', async ({ request }) => {

		const email = uniqueEmail('login');
		const password = randomPassword();

		await register(request, { firstName: 'Ana', lastName: 'QA', email, password });
		const { status, body } = await login(request, email, password);

		expect(status).toBe(200);
		expect(body.data.accessToken).toBeTruthy();

	});

	test('login: password incorrecto devuelve 401', async ({ request }) => {

		const email = uniqueEmail('badpass');
		await register(request, { firstName: 'Ana', lastName: 'QA', email, password: randomPassword() });

		const { status, body } = await login(request, email, 'password-incorrecto');

		expect(status).toBe(401);
		expect(body.success).toBe(false);

	});

	test('login: usuario inexistente devuelve 401', async ({ request }) => {

		const { status } = await login(request, uniqueEmail('missing'), 'whatever');

		expect(status).toBe(401);

	});

	test('refresh: emite un nuevo access token a partir del refresh token', async ({ request }) => {

		const email = uniqueEmail('refresh');
		const password = randomPassword();

		const { body: loginBody } = await register(request, { firstName: 'Ana', lastName: 'QA', email, password });

		const response = await request.post('auth/refresh', {
			data: { refreshToken: loginBody.data.refreshToken }
		});
		const body = await response.json();

		expect(response.status()).toBe(200);
		expect(body.data.accessToken).toBeTruthy();

	});

	test('refresh: token inválido devuelve 401', async ({ request }) => {

		const response = await request.post('auth/refresh', {
			data: { refreshToken: 'esto-no-es-un-token-valido' }
		});

		expect(response.status()).toBe(401);

	});

});
