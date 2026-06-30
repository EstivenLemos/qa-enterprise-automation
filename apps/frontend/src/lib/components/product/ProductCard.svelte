<script lang="ts">

    import type { Product }
    from '$lib/types/product';

    export let product: Product;

</script>

<a
href={`/products/${product.id}`}>

<div
class="border rounded-xl p-4 hover:shadow">

    <h2
    class="font-bold text-xl">

        {product.name}

    </h2>

    <p>

        {product.category}

    </p>

    <p
    class="mt-4">

        ${product.price}

    </p>

</div>

</a>