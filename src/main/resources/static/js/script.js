// Disable Billing Address if Button is Checked
function checkBillingAddress() {
    if ($("#theSameAsShippingAddress").is(":checked")) {
        $(".billingAddress").prop("disabled", true);
    } else {
        $(".billingAddress").prop("disabled", false);
    }
}

// Check New Password Matches
function checkPasswordMatch() {
    var password = $("#txtNewPassword").val();
    var confirmPassword = $("txtConfirmPassword").val();

    if (password == "" && confirmPassword == "") {
        $("#checkPasswordMatch").html("");
        $("#updateUserInfoButton").prop('disabled', true);

    } else {
        if (password != confirmPassword) {
            //$("#checkPasswordMatch").html("Passwords Do Not Match!");
            //$("#updateUserInfoButton").prop('disabled', true);
        } else {
            $("#checkPasswordMatch").html("Passwords Match!");
            //$("#updateUserInfoButton").prop('disabled', false);
        }
    }
}


// Update Cart Quantity to Show Update Button
$(document).ready(function () {
    $(".cartItemQty").on('change', function () {
        var id = this.id;
        $('#update-item-' + id).css('display', 'inline-block');
    });
    $("#theSameAsShippingAddress").on('click', checkBillingAddress);
    $("#txtNewPassword, #txtConfirmPassword").keyup(checkPasswordMatch);
    // $("#textNewPassword").keyup(checkPasswordMatch);
});