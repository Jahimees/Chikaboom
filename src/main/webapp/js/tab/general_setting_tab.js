function openChangeInputFields(thisObj) {
    thisObj.parentElement.querySelector(".confirm").style.display = "block";
    thisObj.parentElement.querySelector(".decline").style.display = "block";
    thisObj.parentElement.querySelector("input").style.display = "block";

    thisObj.parentElement.querySelector("input").value =
        thisObj.parentElement.querySelector(".placeholder").innerHTML.trim();

    thisObj.parentElement.querySelector(".placeholder").style.display = "none";
    thisObj.style.display = "none";
}

function closeChangeInputField(thisObj) {
    thisObj.parentElement.querySelector(".confirm").style.display = "none";
    thisObj.parentElement.querySelector(".decline").style.display = "none";
    thisObj.parentElement.querySelector("input").style.display = "none";

    thisObj.parentElement.querySelector(".placeholder").style.display = "block";
    thisObj.parentElement.querySelector(".change").style.display = "block";
}

function confirmChange(thisObj) {
    obb = thisObj;
    console.log(thisObj);

}