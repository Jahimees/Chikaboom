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

    function wilson_score(up, down) {
        if (!up) return -down;
        let n = up + down;
        let z = 1.64485; //1.0 = 85%, 1.6 = 95%
        let phat = up / n;
        return (phat+z*z/(2*n)-z*Math.sqrt((phat*(1-phat)+z*z/(4*n))/n))/(1+z*z/n);
    }

    function extractTotalName(account) {
        let firstName;
        let lastName;
        let totalName;

        if (typeof account.userDetailsFacade === "undefined") {
            totalName = account.username;
        } else {
            firstName = account.userDetailsFacade.firstName;
            lastName = account.userDetailsFacade.lastName;
            totalName = (firstName ? firstName.trim() + " " : "") + (lastName ? lastName.trim() : "");
            totalName = totalName.trim() ? totalName.trim() : account.username;
        }

        return secureCleanValue(totalName);
    }
}