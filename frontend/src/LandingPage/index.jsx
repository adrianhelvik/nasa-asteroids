import React, { useState, useEffect, useMemo } from "react";
import weeknumber from "current-week-number";
import styled from "styled-components";
import Loading from "react-loading";
import Module from "./Module";
import api from "../api";

const WEEK_COLUMNS = 5;
const WEEK_ROWS = 4;

export default function LandingPage() {
  const [currentWeek] = useState(weeknumber(new Date()));
  const [selectedWeek, setSelectedWeek] = useState(currentWeek);
  const [asteroids, setAsteroids] = useState();

  const weeks = useMemo(() => {
    const weeks = [];
    for (let i = 0; i < WEEK_COLUMNS * WEEK_ROWS; i++) {
      weeks.unshift(currentWeek - i);
    }
    return weeks;
  }, [currentWeek]);

  useEffect(() => {
    let cancelled = false;
    setAsteroids(null);
    api.getAsteroidsInWeek(selectedWeek).then((asteroids) => {
      if (cancelled) return;
      setAsteroids(asteroids);
    });
    return () => {
      cancelled = true;
    };
  }, []);

  return (
    <Container>
      <Header>
        <span className="bx bx-meteor" />
        <h1>Asteroid Watcher</h1>
      </Header>
      <Main>
        <Module title="Week">
          <Grid>
            {weeks.map((week) => (
              <Week
                key={week}
                onClick={() => setSelectedWeek(week)}
                $active={selectedWeek === week}
              >
                {week}
              </Week>
            ))}
          </Grid>
        </Module>
        <Module title="Largest asteroids">
          {asteroids == null && <StyledLoading type="bubbles" />}
        </Module>
      </Main>
    </Container>
  );
}

const Container = styled.main`
  height: 100%;
`;

const Main = styled.main`
  padding: 20px;
  display: grid;
  grid-template-columns: 4fr 6fr;
  grid-auto-flow: row;
  gap: 10px;
  max-width: 900px;
  margin: 0 auto;
`;

const Header = styled.header`
  height: 100px;
  color: var(--accent);
  text-align: center;
  display: flex;
  align-items: center;
  gap: 10px;
  justify-content: center;

  h1 {
    font-weight: normal;
  }

  & > * {
    font-size: 50px;
  }
`;

const Week = styled.button`
  border: none;
  font-weight: normal;
  cursor: pointer;
  aspect-ratio: 1 / 1;
  background-color: ${(p) => (p.$active ? "white" : "#aaa")};
  color: var(--dark);
  border-radius: 5px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 25px;
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: repeat(${WEEK_COLUMNS}, 1fr);
  gap: 10px;
`;

const StyledLoading = styled(Loading)`
  margin: 40px auto;
`;
