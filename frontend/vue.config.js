const { defineConfig } = require('@vue/cli-service')
const webpack = require('webpack');

module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    plugins: [
      new webpack.DefinePlugin({ // It works 🤷‍♂️
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: 'false',
        // Optional flags you might want to add:
        // __VUE_OPTIONS_API__: JSON.stringify(true),
        // __VUE_PROD_DEVTOOLS__: JSON.stringify(false),
      })
    ]
  }
})
