<!-- Common Thymeleaf Fragments for Authentication Forms

This folder contains reusable Thymeleaf fragments for authentication forms.

## Available Fragments

### head.html
Common HTML head section with DOCTYPE, meta tags, title, and CSS link.
**Usage:** `<div th:replace="~{common/head}"></div>`

### form-header.html
Form container header with title.
**Parameters:**
- `formTitle`: The title to display (e.g., "Login", "Register")
**Usage:** `<div th:replace="~{common/form-header :: form-header}"></div>`

### messages.html
Success and logout message displays.
**Usage:** `<div th:replace="~{common/messages}"></div>`

### input-field.html
Reusable input field with label and error display.
**Parameters:**
- `fieldId`: HTML id attribute
- `fieldName`: Field name for th:field
- `fieldLabel`: Label text
- `fieldType`: Input type (text, password, email, etc.)
- `autocomplete`: Autocomplete attribute
- `required`: Whether field is required (true/false)
**Usage:** `<div th:replace="~{common/input-field :: input-field(fieldId='username', fieldName='username', fieldLabel='Username', fieldType='text', autocomplete='username', required=true)}"></div>`

### submit-button.html
Form submit button.
**Parameters:**
- `buttonText`: Text to display on button
**Usage:** `<div th:replace="~{common/submit-button :: submit-button(buttonText='Login')}"></div>`

### form-footer.html
Form footer with link text.
**Parameters:**
- `footerText`: Text before the link
- `footerLinkText`: Link text
- `footerLink`: Thymeleaf URL expression for the link
**Usage:** `<div th:replace="~{common/form-footer :: form-footer(footerText='Don&apos;t have an account?', footerLinkText='Register here', footerLink=@{/register})}"></div>`

## Example Usage

```html
<div th:replace="~{common/head}"></div>
<div th:replace="~{common/form-header :: form-header}"></div>
<div th:replace="~{common/messages}"></div>

<form th:object="${user}">
  <div th:replace="~{common/input-field :: input-field(...)}"></div>
  <div th:replace="~{common/submit-button :: submit-button(...)}"></div>
</form>

<div th:replace="~{common/form-footer :: form-footer(...)}"></div>
```

## Required Model Attributes

Make sure your controller adds these to the model:
- `pageTitle`: Page title for head section
- `formTitle`: Form title for header
- Form object (e.g., `user`) for th:object
-->