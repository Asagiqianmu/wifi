$(function () {
    $(".nanshan_li_div").click(function () {
        $(".nanshan_li_div").parent().css("background", "none");
        $(this).parent().css("background-color", "#EAEAEA");
    })
})


$(function () {
    $('.hot_unit_but').toggle(
          function () {
              $("#hot_but_bg").css("display", "block");
              $("#hot_but_ul").css("display", "block");
          },
          function () {
              $("#hot_but_bg").css("display", "none");
              $("#hot_but_ul").css("display", "none");

          }
);

})
$(function () {
    $("#hot_but_ul li").click(function () {
        $("#hot_but_ul li").find("p").css("color", "#FFFFFF");
        $(this).find("p").css("color", "#36D34D");
    })
})




$(function () {
    $(".android_img").click(function () {
        $(this).attr("src", "../images/android_pressed.png");
    })
    $(".ios_img").click(function () {
        $(this).attr("src", "../images/ios_pressed.png");
    })
})

$(function () {
    $(".wifi_but").click(function () {
        $(this).css("background-color", "#40D355");
    })
})







$(function () {
    $(".mian_tit").hover(
function () {
    $(this).parent().css('opacity', '0.8');
},
function () {
    $(this).parent().css('opacity', '1');
}
);
})

//20151029搜索框
$(function () {
    $('.number').bind({
        focus: function () {
            if (this.value == this.defaultValue) {
                this.value = "";
            }
        },
        blur: function () {
            if (this.value == "") {
                this.value = this.defaultValue;
            }
        }
    });
})



$(function () {
    $('.feedtext').bind({
       
        focus: function () {
            if (this.value == this.defaultValue) {
                this.value = "";
            }
        },
        blur: function () {
            if (this.value == "") {
                this.value = this.defaultValue;
            }
        }
    });
})


//20160106

$(function () {
    $('.map').bind({
        focus: function () {
            if (this.value == this.defaultValue) {
                this.value = "";
            }
        },
        blur: function () {
            if (this.value == "") {
                this.value = this.defaultValue;
            }
        }
    });
})

/*$(function () {
    $('.back_portal').click(function () {
        $('.select_video').fadeIn("fast").delay(2000).hide(200);
        $('.select_bg').fadeIn("slow").delay(2000).hide(200);
    })
})*/
