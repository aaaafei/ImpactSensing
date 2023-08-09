<template>
   <div>
		 <iframe  :src="iframe_url" height="500" width="100%" frameborder="no" border="0" ></iframe>
    </div>
</template>

<script>
   /* eslint-disable */
 export default{ 
	  	data(){
			return {
				user: null,
				iframe_url: null
			};
	  	},
	  	 methods: {
     		getUser: function () {
        		this.user = this.$store.getters.getUserInfo; 
      		}
      	},
	    mounted: function () {
	      var self = this;
	        // this.setLayoutAndTheme();
	        this.$nextTick(function () {
	            self.getUser();  
	            var hostport = document.location.host;
	            self.iframe_url = 'http://' + hostport + '/materials/app/wzManage.do?reqCode=initCK&userName=' + encodeURI(self.user.truename) + '&deptName=' + encodeURI(self.user.organname);

	        });   
	    }
   }; 
</script>