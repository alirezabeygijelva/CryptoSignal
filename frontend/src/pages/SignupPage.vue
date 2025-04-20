<template>
  <q-page class="row justify-center q-py-lg">
    <div class="q-pa-md col-lg-8 col-md-8 col-sm-10 col-xs-12">
      <q-card flat bordered>
        <q-card-section>
          <div class="text-h6">{{ $t('signup.title') }}</div>
        </q-card-section>

        <q-card-section class="q-pt-none q-gutter-sm">
          <q-input v-model="firstNameModel" :label="$t('signup.firstName')" standout="bg-grey-6 text-white" :hint="$t('formHints.firstName')" />
          <q-input v-model="lastNameModel" :label="$t('signup.lastName')" standout="bg-grey-6 text-white" :hint="$t('formHints.lastName')" />
          <q-input v-model="emailModel" :label="$t('signup.email')" standout="bg-grey-6 text-white" :hint="$t('formHints.username')" />
          <q-input v-model="phoneModel" :label="$t('signup.phone')" standout="bg-grey-6 text-white" :hint="$t('formHints.phone')" />
          <q-input v-model="passwordModel" :label="$t('signup.password')" standout="bg-grey-6 text-white" :hint="$t('formHints.password')" />
          <q-input v-model="confirmPasswordModel" :label="$t('signup.confirmPassword')" standout="bg-grey-6 text-white" :hint="$t('formHints.password')" />
        </q-card-section>

        <q-card-actions class="q-pt-none q-px-md q-pb-md">
          <q-btn color="positive" icon-right="person_add" :label="$t('signup.submit')" @click="onSignup" />
        </q-card-actions>

        <q-separator inset/>

        <q-card-section>
          {{ $t('signup.haveAccount') }}
          <router-link to="/login">{{ $t('signup.login') }}</router-link>
        </q-card-section>
      </q-card>
    </div>
  </q-page>
</template>

<script>
export default {
  name: "SignupPage",

  data() {
    return {
      firstNameModel: '',
      lastNameModel: '',
      emailModel: '',
      phoneModel: '',
      passwordModel: '',
      confirmPasswordModel: ''
    }
  },

  methods: {
    onSignup() {
      this.$q.loading.show()
      this.$api.signup(this.firstNameModel, this.lastNameModel, this.emailModel, this.phoneModel, this.passwordModel, this.confirmPasswordModel)
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('notifications.successfulSignup'),
            type: 'positive'
          })
          this.$cookies.set('access_token', response.data.data.accessToken, response.data.data.expiresIn)
          this.$cookies.set('refresh_token', response.data.data.refreshToken, 30 * 24 * 60 * 60)
          this.$router.go()
        })
        .catch((error) => {
          this.$q.notify({
            progress: true,
            message: this.$t(`errors.${error.response.data.error.kind}`),
            type: 'negative'
          })
        })
        .finally(() => {
          this.$q.loading.hide()
        })
    }
  }
}
</script>


<style scoped>

</style>
