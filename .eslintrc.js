module.exports = {
  root: true,
  extends: "@react-native",
  rules: {
    "no-shadow": "off",
    "@typescript-eslint/no-shadow": "error",
    quotes: [
      "warn",
      "double",
      {
        avoidEscape: true,
      },
    ],
    "max-len": ["warn", 140],
    "no-unused-vars": ["off"],
    "@typescript-eslint/no-unused-vars": ["warn"],
    "react/function-component-definition": [
      "error",
      {
        namedComponents: "function-declaration",
      },
    ],
    "prettier/prettier": ["warn"],
    "no-trailing-spaces": "off",
  },
};
