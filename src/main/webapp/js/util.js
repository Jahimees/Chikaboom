function secureCleanValue(value) {
    value = value.replaceAll('<', '').replaceAll('>', '').replaceAll('script', '').replaceAll('/', '');

    return value;
}