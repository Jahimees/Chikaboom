function initPhoneCodeWidget(inputId) {

    const input = document.querySelector("#" + inputId);
    const errorMsg = document.querySelector("#error-msg-" + inputId);

    const errorMap = ["Неверный номер", "Неверный код страны", "Слишком короткий номер", "Слишком длинный номер", "Неверный номер"];

    const iti = window.intlTelInput(input, {
        hiddenInput: "full_phone",
        initialCountry: "auto",
        geoIpLookup: callback => {
            fetch("https://ipapi.co/json")
                .then(res => res.json())
                .then(data => callback(data.country_code))
                .catch(() => callback("by"));
        },
        utilsScript: "https://cdn.jsdelivr.net/npm/intl-tel-input@18.2.1/build/js/utils.js?1690975972744",
    });

    const reset = () => {
        input.classList.remove("error");
        errorMsg.innerText = "";
        errorMsg.classList.add("hide");
    };

    input.addEventListener('keyup', () => {
        reset();
        if (input.value.trim()) {
            if (iti.isValidNumber()) {
                input.style.borderColor = "";
            } else {
                input.style.borderColor = "red";
                input.classList.add("error");
                const errorCode = iti.getValidationError();

                //undefined error
                if (errorCode !== -99) {
                    errorMsg.innerText = errorMap[errorCode];
                } else {
                    errorMsg.innerText = "Неверный формат номера телефона";
                }
                errorMsg.classList.remove("hide");

            }
        }
    });

    input.addEventListener('change', reset);
}
