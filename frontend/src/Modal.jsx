import styled, { css } from "styled-components";
import React from "react";

export default function Modal({ $visible, onBackDropClick, children }) {
  return (
    <>
      <Backdrop $visible={$visible} onClick={onBackDropClick} />
      <Menu $visible={$visible}>{children}</Menu>
    </>
  );
}

const Menu = styled.div`
  position: absolute;
  left: 50%;
  transform-origin: 50% 50%;
  transform: translateX(-50%) translateY(-90%) scale(1);
  overflow: hidden;
  z-index: 1;

  box-shadow: var(--box-shadow-4);
  transition: 0.25s;

  ${(p) =>
    !p.$visible &&
    css`
      transform: translateX(-50%) translateY(-90%) scale(0);
      border-radius: 1000px;
      pointer-events: none;
      opacity: 0;
    `};
`;

const Backdrop = styled.div`
  position: fixed;
  bottom: 0;
  right: 0;
  left: 0;
  top: 0;
  background-color: rgba(0, 0, 0, 0.5);
  transition: opacity 0.5s;
  z-index: 1;

  ${(p) =>
    !p.$visible &&
    css`
      pointer-events: none;
      opacity: 0;
    `};
`;
