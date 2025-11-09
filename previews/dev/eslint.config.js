// eslint.config.js (Flat Config for ESLint 9)
import js from "@eslint/js";

export default [
    js.configs.recommended,
    {
        files: ["**/*.js"],
        languageOptions: {
            ecmaVersion: "latest",
            sourceType: "module",
            globals: {
                window: "readonly",
                document: "readonly",
                fetch: "readonly",
                console: "readonly"
            }
        },
        rules: {
            "no-unused-vars": ["warn", { vars: "all", args: "none" }],
            "no-undef": "error"
        }
    }
];
