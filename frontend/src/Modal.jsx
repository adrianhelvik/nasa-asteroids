import styled, { keyframes, css } from "styled-components";
import React from "react";

export default function Modal({ $visible, onBackDropClick, children }) {
  return (
    <Dialog open={$visible}>
      <Backdrop $visible={$visible} onClick={onBackDropClick} />
      <Menu $visible={$visible}>{children}</Menu>
    </Dialog>
  );
}

const Dialog = styled.dialog`
  border: none;
  background: transparent;
`;

const Menu = styled.div`
  position: fixed;
  left: 50%;
  top: max(0px, calc(50% - 100px));
  transform-origin: 50% 50%;
  overflow: hidden;
  z-index: 1;

  ${(p) =>
    p.$visible &&
    css`
      animation: ${keyframes`
        from {
          transform: translateX(-50%) translateY(-50%) scale(0);
          border-radius: 1000px;
          opacity: 0;
        }
        to {
          transform: translateX(-50%) translateY(-50%) scale(1);
          border-radius: 0;
          opacity: 1;
        }
      `} 0.5s forwards;
    `};

  ${(p) =>
    !p.$visible &&
    css`
      animation: ${keyframes`
        from {
          transform: translateX(-50%) translateY(-50%) scale(1);
          border-radius: 0;
          opacity: 1;
        }
        to {
          transform: translateX(-50%) translateY(-50%) scale(0);
          border-radius: 1000px;
          opacity: 0;
        }
      `} 0.5s forwards;
    `};

  box-shadow: var(--box-shadow-4);

  ${(p) =>
    !p.$visible &&
    css`
      pointer-events: none;
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
