console.log("this is Script file")

const togglesidebar=()=>{
    
   if($(".sidebar").is(":visible")){
       //true
       //band karna hai

       $(".sidebar").css("display", "none");
       $(".content").css("margin-left", "0%");

   }else{
       //flase
       //show karna hai
       $(".sidebar").css("display", "block");
       $(".content").css("margin-left", "20%");

   }

};