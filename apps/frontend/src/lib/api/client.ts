import { PUBLIC_API_URL }
from '$env/static/public';

export async function api<T>(
    endpoint: string
): Promise<T> {

    const response =
        await fetch(
            `${PUBLIC_API_URL}${endpoint}`
        );

    if (!response.ok) {

        throw new Error(
            `HTTP ${response.status}`
        );

    }

    return response.json();

}