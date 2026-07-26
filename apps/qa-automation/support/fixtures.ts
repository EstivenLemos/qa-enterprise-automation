import { test as base, expect } from '@playwright/test';

// El fixture `request` incorporado de Playwright es de ámbito "worker" (se comparte
// entre todos los tests del mismo worker) y reutiliza la conexión HTTP subyacente.
// El proxy de puertos de Docker Desktop en Windows no maneja bien esa reutilización
// para respuestas chunked: la primera petición del worker funciona, todas las
// siguientes devuelven el cuerpo truncado. Se reemplaza por un contexto nuevo por test.
export const test = base.extend({
	request: async ({ playwright, baseURL }, use) => {

		const context = await playwright.request.newContext({
			baseURL,
			extraHTTPHeaders: {
				'Content-Type': 'application/json',
				Connection: 'close'
			}
		});

		await use(context);
		await context.dispose();

	}
});

export { expect };
