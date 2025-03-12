module.exports = {
  e2e: {
    baseUrl: 'http://localhost:4200', // URL deiner Angular-App
    supportFile: false, // Optional
    reporter: 'mochawesome', // Verwenden des mochawesome Reporters
    reporterOptions: {
      reportDir: 'cypress/reports',
      overwrite: false,
      html: true,
      json: true
    },
  }
}
