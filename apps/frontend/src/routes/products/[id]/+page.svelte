<script lang="ts">

import { page }
from '$app/state';

import { onMount }
from 'svelte';

import {
    getProductById
}
from '$lib/api/product-api';

import type {
    Product
}
from '$lib/types/product';

let product: Product | null = null;

onMount(async () => {

    product =
        await getProductById(
            Number(
                page.params.id
            )
        );

});

</script>