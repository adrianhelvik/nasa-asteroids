import { useNavigate, useLocation } from "react-router-dom";
import { useCallback, useRef } from "react";

export default function useSearchState(name, defaultValue, config = {}) {
  const { search } = useLocation();
  const params = new URLSearchParams(search);
  const navigate = useNavigate();
  const value = params.get(name);

  const defaultValueRef = useRef();

  defaultValueRef.current = defaultValue;

  // TODO: Use useEvent when that becomes available

  const realSetter = (value) => {
    const params = new URLSearchParams(window.location.search);
    if (typeof value === "function") {
      value = value(JSON.parse(params.get(name) ?? defaultValue));
    }
    params.set(name, JSON.stringify(value));
    navigate("?" + params.toString(), config);
  };

  const realSetterRef = useRef();
  realSetterRef.current = realSetter;

  const setter = useCallback((value) => {
    realSetterRef.current(value);
  }, []);

  let parsedValue;

  if (value !== null) {
    try {
      parsedValue = JSON.parse(value);
    } catch (e) {
      // Parsing failed. Revert to defaultValue.
    }
  }

  return [parsedValue === undefined ? defaultValue : parsedValue, setter];
}
