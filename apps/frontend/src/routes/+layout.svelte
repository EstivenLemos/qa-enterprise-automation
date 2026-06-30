<script lang="ts">

    import '../app.css';

    import Navbar
    from '$lib/components/layout/Navbar.svelte';

</script>

<Navbar />

<main
class="container mx-auto p-6">

    <slot />

</main>