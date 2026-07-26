function uniqueSuffix(): string {
	return `${Date.now()}-${Math.floor(Math.random() * 100000)}`;
}

export function uniqueEmail(prefix = 'qa'): string {
	return `${prefix}-${uniqueSuffix()}@smartstore.test`;
}

export function uniqueName(prefix = 'Item'): string {
	return `${prefix}-${uniqueSuffix()}`;
}

export function randomPassword(): string {
	return `Secret-${uniqueSuffix()}`;
}
