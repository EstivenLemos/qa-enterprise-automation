<script lang="ts">

	let email = '';

	let password = '';

	function login() {

		console.log(email);

		console.log(password);

	}

</script>

<div class="flex justify-center mt-20">

	<div class="w-96">

		<h1 class="text-3xl mb-5">

			Login

		</h1>

		<input
			bind:value={email}
			placeholder="Email"
			class="border p-3 w-full mb-4"
		/>

		<input
			type="password"
			bind:value={password}
			placeholder="Password"
			class="border p-3 w-full mb-4"
		/>

		<button
			on:click={login}
			class="bg-blue-500 text-white p-3 w-full"
		>

			Login

		</button>

	</div>

</div>