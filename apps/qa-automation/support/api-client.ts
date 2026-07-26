import type { APIRequestContext, APIResponse } from '@playwright/test';

export interface ApiEnvelope<T> {
	success: boolean;
	message: string;
	data: T;
	timestamp: string;
}

export interface AuthResponseBody {
	accessToken: string;
	refreshToken: string;
}

export interface RegisterPayload {
	firstName: string;
	lastName: string;
	email: string;
	password: string;
}

async function toEnvelope<T>(response: APIResponse): Promise<ApiEnvelope<T>> {
	return (await response.json()) as ApiEnvelope<T>;
}

export async function register(request: APIRequestContext, payload: RegisterPayload) {
	const response = await request.post('auth/register', { data: payload });
	return { status: response.status(), body: await toEnvelope<AuthResponseBody>(response) };
}

export async function login(request: APIRequestContext, email: string, password: string) {
	const response = await request.post('auth/login', { data: { email, password } });
	return { status: response.status(), body: await toEnvelope<AuthResponseBody>(response) };
}

export async function loginAsAdmin(request: APIRequestContext): Promise<string> {
	const email = process.env.ADMIN_SEED_EMAIL ?? 'admin@smartstore.com';
	const password = process.env.ADMIN_SEED_PASSWORD ?? 'Admin123!';

	const { status, body } = await login(request, email, password);

	if (status !== 200) {
		throw new Error(
			`No se pudo autenticar como ADMIN sembrado (${email}). ¿El backend levantó con DataSeeder? Status: ${status}`
		);
	}

	return body.data.accessToken;
}

export function authHeader(token: string): Record<string, string> {
	return { Authorization: `Bearer ${token}` };
}
