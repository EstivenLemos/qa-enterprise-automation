import { defineConfig } from '@playwright/test';
import 'dotenv/config';

export default defineConfig({
	testDir: './tests',
	fullyParallel: true,
	retries: process.env.CI ? 1 : 0,
	reporter: [
		['list'],
		['allure-playwright', { resultsDir: 'allure-results' }]
	],
	use: {
		// OJO: debe terminar en "/" y las rutas de las requests NO deben empezar con "/".
		// Si el path empieza con "/", la resolución de URL (WHATWG) lo trata como absoluto
		// desde el origen y descarta el "/api" del baseURL (bug real que costó diagnosticar
		// esta suite: las requests le pegaban a "/categories" en vez de "/api/categories",
		// Spring Security las rechazaba con 403 y el body vacío rompía el .json()).
		baseURL: (process.env.BASE_URL ?? 'http://localhost:8080/api').replace(/\/?$/, '/')
	}
});
