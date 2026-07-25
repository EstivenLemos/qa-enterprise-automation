import { writable }
from 'svelte/store';

import type { Product }
from '$lib/types/product';

export const products =
    writable<Product[]>([]);