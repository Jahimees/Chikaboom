//TODO нет валидации в настройках.
// Задеты
// create_client_modal.jsp
// client_tab.jsp
// dynamic_popup.js
// client.js
// personality.jsp
// tab.js
// client_info_modal.jsp
// income|outcomeappointments jsp
// appointment.js
// common.css
// SQL user_details 15 - 30
// JAVA
// UserDetailsDataService
// PhoneNumberUtils
// AccountDataService
// PhoneCodeRepository

function initPhoneCodeWidget(inputId) {

    const input = document.querySelector("#" + inputId);
    const errorMsg = document.querySelector("#error-msg-" + inputId);

    const errorMap = ["Неверный номер", "Неверный код страны", "Слишком короткий номер", "Слишком длинный номер", "Неверный номер"];

    const iti = window.intlTelInput(input, {
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
                console.log("Problems")
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
