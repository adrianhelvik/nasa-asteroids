import HeartBrokenIcon from "@mui/icons-material/HeartBroken";
import React, { useState, useEffect, useMemo } from "react";
import fmtDiameter from "../fmtDiameter";
import styled from "styled-components";
import { getISOWeek } from "date-fns";
import Loading from "react-loading";
import Module from "./Module";
import Modal from "../Modal";
import api from "../api";

const WEEK_COLUMNS = 5;
const WEEK_ROWS = 4;

export default function LandingPage() {
  const [currentWeek] = useState(getISOWeek(new Date()));
  const [selectedWeek, setSelectedWeek] = useState(currentWeek);
  const [potentiallyHazardous, setOnlyHazardous] = useState(false);
  const [menuVisible, setMenuVisible] = useState(false);
  const [asteroids, setAsteroids] = useState();
  const [error, setError] = useState();

  useEffect(() => {
    const onKeyUp = (event) => {
      if (event.key === "ArrowLeft") {
        setError();
        setSelectedWeek((week) => Math.max(week - 1, 1));
      }
      if (event.key === "ArrowRight") {
        setError();
        setSelectedWeek((week) => Math.min(week + 1, currentWeek));
      }
    };
    document.addEventListener("keyup", onKeyUp);
    return () => {
      document.removeEventListener("keyup", onKeyUp);
    };
  }, [currentWeek]);

  const weeks = useMemo(() => {
    const weeks = [];
    for (let i = 0; i < WEEK_COLUMNS * WEEK_ROWS; i++) {
      if (currentWeek - i >= 1) {
        weeks.unshift(currentWeek - i);
      } else {
        // I would have handled weeks around the year change
        // better here.
        break;
      }
    }
    return weeks;
  }, [currentWeek]);

  useEffect(() => {
    if (error) return;
    let cancelled = false;
    setAsteroids(null);
    api.getAsteroidsInWeek(selectedWeek, { potentiallyHazardous }).then(
      ({ data }) => {
        if (cancelled) return;
        setAsteroids(data);
      },
      (error) => {
        setError(error.message);
      }
    );
    return () => {
      cancelled = true;
    };
  }, [selectedWeek, error, potentiallyHazardous]);

  console.log(asteroids);

  return (
    <Container>
      <Header>
        <span className="bx bx-meteor" />
        <h1>Nearest asteroids</h1>
      </Header>
      <SelectedWeekWrapper onClick={() => setMenuVisible((_) => !_)}>
        <SelectedWeek>{selectedWeek}</SelectedWeek>
      </SelectedWeekWrapper>
      <KeyboardHelp>
        <h4>Shortcuts</h4>
        <div className="items">
          <div>
            <i className="bx bx-caret-left-square" />
          </div>
          <div>
            <i className="bx bx-caret-right-square" />
          </div>
        </div>
      </KeyboardHelp>
      <Label>
        <input
          type="checkbox"
          checked={potentiallyHazardous}
          onChange={(e) => setOnlyHazardous(e.target.checked)}
        />
        Only potentially hazardous
      </Label>
      <Modal
        $visible={menuVisible}
        onBackDropClick={() => setMenuVisible(false)}
      >
        <Module title="Select week">
          <Grid>
            {weeks.map((week) => (
              <Week
                key={week}
                onClick={() => {
                  setSelectedWeek(week);
                  setMenuVisible(false);
                }}
                $active={selectedWeek === week}
              >
                {week}
              </Week>
            ))}
          </Grid>
        </Module>
      </Modal>
      <Main>
        {asteroids == null && !error && <StyledLoading type="bubbles" />}
        {asteroids == null && error && (
          <ErrorMessage>
            <HeartBrokenIcon />
            <div>{error}</div>
            <Button onClick={() => setError(null)}>Retry</Button>
          </ErrorMessage>
        )}
        <AsteroidList>
          {asteroids?.map?.((asteroid) => (
            <Asteroid
              key={asteroid.id}
              className={
                asteroid.is_potentially_hazardous_asteroid ? "dangerous" : ""
              }
            >
              <div>
                <AsteroidName>
                  <span className="bx bx-meteor" /> {asteroid.name}{" "}
                </AsteroidName>
                <div>
                  <strong>Distance:</strong>{" "}
                  {new Intl.NumberFormat("en-US").format(
                    Math.round(asteroid.miss_distance_km)
                  )}{" "}
                  km
                </div>
                <div>
                  <strong>Diameter:</strong>{" "}
                  {fmtDiameter(asteroid.estimated_diameter)}
                </div>
              </div>
              <a href={`/asteroid/${asteroid.id}`}>See more</a>
            </Asteroid>
          ))}
        </AsteroidList>
      </Main>
    </Container>
  );
}

const Container = styled.main`
  height: 100%;
`;

const Main = styled.main`
  padding: 20px;
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

    @media (max-width: 500px) {
      font-size: 40px;
    }
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

const AsteroidList = styled.div`
  display: grid;
  gap: 20px;
  grid-template-columns: repeat(2, 1fr);
`;

const Asteroid = styled.div`
  align-items: center;
  background-color: white;
  border-radius: 5px;
  padding: 10px;
  color: var(--dark);
  display: grid;
  grid-template-columns: 1fr auto;
  a {
    background-color: var(--dark-accent);
    color: white;
    padding: 10px;
    border-radius: 5px;
    text-decoration: none;
    cursor: pointer;
    transition: background-color 0.1s;
    :hover {
      background-color: var(--mid-accent);
    }
    :hover:active {
      background-color: var(--dark);
    }
  }

  .bx {
    font-size: 30px;
  }

  &.dangerous .bx {
    color: var(--red, red);
  }
`;

const ErrorMessage = styled.div`
  text-align: center;
  margin: 50px;
`;

const Button = styled.button`
  border-radius: 5px;
  border: none;
  background-color: white;
  color: var(--dark);
  padding: 3px 20px;
  margin-top: 15px;
  cursor: pointer;

  :hover {
    background-color: #eee;
  }

  :hover:active {
    background-color: #aaa;
  }
`;

const AsteroidName = styled.h3`
  margin: 0;
  font-size: 20px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  gap: 10px;
`;

const SelectedWeekWrapper = styled.button`
  border: none;
  cursor: pointer;
  background-color: var(--dark-accent);
  color: white;
  padding: 20px;
  margin: 0 auto;
  width: 150px;
  height: 150px;
  border-radius: 200px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: background-color 0.1s;
  :hover {
    background-color: var(--mid-accent);
  }
  :hover:active {
    background-color: var(--very-dark-accent);
  }
`;

const SelectedWeek = styled.div`
  font-size: 70px;
  text-align: center;
  line-height: 1.05;
  font-weight: bold;
  ::before {
    content: "WEEK";
    display: block;
    font-size: 20px;
    opacity: 0.5;
    letter-spacing: 4px;
    font-weight: normal;
  }
`;

const KeyboardHelp = styled.div`
  width: fit-content;
  margin: 0 auto;
  margin-top: 10px;
  color: white;
  opacity: 50%;
  text-align: center;

  h4 {
    font-weight: normal;
    margin: 0;
    margin-bottom: 5px;
    font-size: 14px;
  }

  .items {
    font-size: 30px;
    display: flex;
    gap: 10px;
    justify-content: center;
  }
`;

const Label = styled.label`
  display: block;
  width: fit-content;
  margin: 0 auto;
  background-color: var(--red);
  color: white;
  padding: 10px;
  border-radius: 5px;
`;
