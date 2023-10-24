{
    function secureCleanValue(value) {
        if (value !== null && value !== "" && typeof value === 'string') {
            value = value.replaceAll('<', '').replaceAll('>', '').replaceAll('script', '').replaceAll('/', '');
        }

        return value;
    }

    Date.prototype.daysInMonth = () => {
        const date = new Date();
        return 32 - new Date(date.getFullYear(), date.getMonth(), 32).getDate();
    };
}