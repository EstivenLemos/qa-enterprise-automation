import adapter from '@sveltejs/adapter-node';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';

export default {

	preprocess: vitePreprocess(),

	kit: {

		adapter: adapter(),

		alias: {

			$components: 'src/lib/components',

			$api: 'src/lib/api',

			$stores: 'src/lib/stores',

			$types: 'src/lib/types',

			$utils: 'src/lib/utils',

			$validators: 'src/lib/validators'
		}
	}
};