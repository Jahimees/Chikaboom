function secureCleanValue(value) {
    if (value !== null && value !== "" && typeof value === 'string') {
        value = value.replaceAll('<', '').replaceAll('>', '').replaceAll('script', '').replaceAll('/', '');
    }

    return value;
}