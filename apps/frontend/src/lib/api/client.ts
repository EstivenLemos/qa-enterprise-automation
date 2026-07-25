import { PUBLIC_API_URL }
from '$env/static/public';

interface ApiResponse<T> {
    success: boolean;
    message: string;
    data: T;
    timestamp: string;
}

export async function api<T>(
    endpoint: string,
    init?: RequestInit
): Promise<T> {

    const response =
        await fetch(
            `${PUBLIC_API_URL}${endpoint}`,
            init
        );

    const body: ApiResponse<T> = await response.json();

    if (!response.ok || !body.success) {

        throw new Error(
            body.message ?? `HTTP ${response.status}`
        );

    }

    return body.data;

}