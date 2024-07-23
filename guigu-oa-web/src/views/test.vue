<template>
  <div>
    <div>账号切换</div>
    <button @click="zhangsan()" type="default" size="mini">张三</button>
    <button @click="lisi()" type="default" size="mini">李四</button>
    <button @click="zzjl()" type="default" size="mini">张学科</button>
    <button @click="admin()" type="default" size="mini">admin</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDcyNjQysjC30FEqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQBDl3NrQgAAAA.4wx0L-MSzpgMEVWtLjGmmz4jZT4aZ3pkFENuG6_-erkZ61LbXcVnXjUpxalC29_O7zh-nbGlVVDF2blA9LEmWQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zhangsan() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDcyNjQysjS00FEqLU4t8kxRsjKHMPMSc1OBWqoyEvPSixPzlGoB3TQS20UAAAA.NZ_KXWcQuYK-bWtZInHY2wgB0VK-c_v2_eSEQTa1sa8TpwnTiU9QOUuc-_5AopVPZP-3cb75h47RcdhUeajy1g'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zzjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDcyNjSzMDex0FEqLU4t8kxRsjKDMPMSc1OBWiqy06qKlGoBgrBnzEIAAAA.iWyiCOZG-duNQ44qdJrBrWso4acE4BSapJIpCxR_bP5LsqPwWa4CAfJwhpXoaI8g6ZkHbzrEVi_yMvjrQDFMuQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lisi() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDcyNjQysjQx01EqLU4t8kxRsjKBMPMSc1OBWnIyizOVagGZAHTBQQAAAA.w93a5kxooTGHditC-jaCTG4GvkBH2KJsnamdiO1dPw2tupjrAs8lUeYyCfBWI4PD-IlWjwpPd-qLN7Fh0uPj1A'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
