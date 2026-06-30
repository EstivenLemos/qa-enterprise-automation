<script lang="ts">

import { onMount }
from 'svelte';

import ProductCard
from '$lib/components/product/ProductCard.svelte';

import {
    getProducts
}
from '$lib/api/product-api';

import type {
    Product
}
from '$lib/types/product';

let products: Product[] = [];

onMount(async () => {

    products =
        await getProducts();

});

</script>