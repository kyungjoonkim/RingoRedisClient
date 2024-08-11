<template>
  <div>
    <div>
      <h2>Please Log In</h2>
      <div id="loginForm">
        <form @submit.prevent="fnLogin">
          <p>
            <input class="w3-input" name="uid" placeholder="Enter your ID"  v-model="userID" ><br>
          </p>
          <p>
            <input name="password" class="w3-input" placeholder="Enter your password" v-model="userPW" type="password">
          </p>
          <p>
            <button type="submit" class="w3-button w3-green w3-round">Login</button>
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { ref } from 'vue'
  const userID = ref('')
  const userPW = ref('')

  const fnLogin = () =>{

    console.log(`userID: ${userID.value}, userPW: ${userPW.value}`)
    if( !userID.value ){
      alert('ID를 입력하세요.')
      return false;
    }

    if( !userPW.value ){
      alert('비밀번호를 입력하세요.')
      return false;
    }

    alert('로그인 되었습니다.')
  }


</script>

<style>
#loginForm {
  width: 500px;
  margin: auto;
}
</style>