import { api }
from './client';

import type { Product }
from '$lib/types/product';

export async function getProducts() {

    return api<Product[]>(
        '/products'
    );

}

export async function getProductById(
    id: number
) {

    return api<Product>(
        `/products/${id}`
    );

}