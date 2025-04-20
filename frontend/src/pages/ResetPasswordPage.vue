<template>
  <q-page class="row justify-center q-py-lg">
    <div class="q-pa-md col-lg-8 col-md-8 col-sm-10 col-xs-12">
      <q-card flat bordered>
        <q-card-section>
          <div class="text-h6">{{ $t('resetPassword.title') }}</div>
        </q-card-section>

        <q-card-section class="q-pt-none q-gutter-sm">
          <q-input v-model="password" :label="$t('resetPassword.password')" standout="bg-grey-6 text-white" :hint="$t('formHints.password')" />
          <q-input v-model="confirmPassword" :label="$t('resetPassword.confirmPassword')" standout="bg-grey-6 text-white" :hint="$t('formHints.confirmPassword')" />
        </q-card-section>

        <q-card-actions class="q-pt-none q-px-md q-pb-md">
          <q-btn color="positive" :label="$t('resetPassword.submit')" @click="onResetPassword" />
        </q-card-actions>
      </q-card>

    </div>
  </q-page>
</template>

<script>
export default {
  name: "ResetPasswordPage",

  data() {
    return {
      resetToken: undefined,
      password: undefined,
      confirmPassword: undefined
    }
  },

  methods: {
    onResetPassword() {
      this.$q.loading.show()
      this.$api.resetPassword(this.password, this.confirmPassword, this.resetToken)
        .then((response) => {
          this.$q.notify({
            progress: true,
            message: this.$t('notifications.successfulResetPassword'),
            type: 'positive'
          })
          this.$router.push({path: "/login"})
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
  },

  mounted() {
    this.resetToken = this.$route.query.token
  }
}
</script>

<style scoped>

</style>
