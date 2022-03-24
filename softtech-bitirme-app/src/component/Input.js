import React from "react";
import { Form } from "semantic-ui-react";

const Input = (props) => {
  const { label, error, name, onChange, type, flag , value, placeholder, style } = props;
  return (
    <Form.Field>
      <label>{label}</label>
      <Form.Input
        style={style}
        type={type}
        name={name}
        required={flag}
        value={value}
        onChange={onChange}
        error={error}
        placeholder={placeholder}
      ></Form.Input>
    </Form.Field>
  );
};

export default Input;
