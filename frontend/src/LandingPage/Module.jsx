import styled from "styled-components";
import React from "react";

export default function Module({ title, children }) {
  return (
    <Container>
      <Title>{title}</Title>
      <Content>{children}</Content>
    </Container>
  );
}

const Container = styled.div`
  --padding: 10px;
  --border-radius: 5px;

  max-height: 80vh;
  overflow: auto;
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
`;

const Title = styled.h2`
  padding: var(--padding);
  margin: 0;
  font-size: 20px;
  background-color: var(--accent);
  color: var(--dark);
  border-top-left-radius: var(--border-radius);
  border-top-right-radius: var(--border-radius);
  position: sticky;
  top: 0;
`;

const Content = styled.div`
  padding: var(--padding);
  background-color: var(--dark-80);
  border-bottom-left-radius: var(--border-radius);
  border-bottom-right-radius: var(--border-radius);
  font-size: 16px;
`;
