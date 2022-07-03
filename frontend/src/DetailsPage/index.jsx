import HeartBrokenIcon from "@mui/icons-material/HeartBroken";
import React, { useEffect, useState, useMemo } from "react";
import WarningIcon from "@mui/icons-material/Warning";
import CheckIcon from "@mui/icons-material/Check";
import { useNavigate } from "react-router-dom";
import { useParams } from "react-router-dom";
import fmtDiameter from "../fmtDiameter";
import styled from "styled-components";
import Loading from "react-loading";
import api from "../api";

export default function DetailsPage() {
  const [asteroid, setAsteroid] = useState(null);
  const [error, setError] = useState();
  const navigate = useNavigate();
  const { id } = useParams();

  useEffect(() => {
    if (error) return;
    let cancelled = false;
    let retries = 0;

    const retrieve = () => {
      api.getAsteroidById(id).then(
        (asteroid) => {
          if (cancelled) return;
          setAsteroid(asteroid);
        },
        (error) => {
          if (cancelled) return;
          if (retries > 5) {
            setAsteroid(null);
            setError(error.message);
          } else {
            setTimeout(() => {
              retries += 1;
              retrieve();
            }, 200);
          }
        }
      );
    };

    retrieve();

    return () => {
      cancelled = true;
    };
  }, [id, error]);

  const view = useMemo(() => {
    if (error) return "error";
    if (asteroid) return "asteroid";
    return "loading";
  }, [error, asteroid]);

  return (
    <div>
      <Header>
        <HomeLink
          href="/"
          onClick={(e) => {
            e.preventDefault();
            navigate(-1);
          }}
        >
          <span className="bx bx-meteor" />
          <span className="title-text">Go back</span>
        </HomeLink>
        <H1>{asteroid?.name}</H1>
      </Header>
      {view === "loading" && <StyledLoading type="bubbles" />}
      {view === "error" && (
        <ErrorMessage>
          <HeartBrokenIcon />
          <div>{error}</div>
          <Button onClick={() => setError(null)}>Try again</Button>
        </ErrorMessage>
      )}
      {view === "asteroid" && (
        <Main>
          {asteroid.is_potentially_hazardous_asteroid && (
            <DangerStatus>
              <WarningIcon style={{ fontSize: 30 }} /> Potentially hazardous!
            </DangerStatus>
          )}
          {!asteroid.is_potentially_hazardous_asteroid && (
            <SafeStatus>
              <CheckIcon style={{ fontSize: 30 }} /> <span>Not hazardous</span>
            </SafeStatus>
          )}
          <Grid>
            <InfoBox>
              <InfoHeader>
                <div className="bx bx-ruler" />
                <div>Nearest miss</div>
              </InfoHeader>
              <InfoContent>
                {new Intl.NumberFormat("en-US").format(
                  Math.round(asteroid.miss_distance_km)
                )}{" "}
                km
              </InfoContent>
            </InfoBox>
            <InfoBox>
              <InfoHeader>
                <div className="bx bx-circle" />
                <div>Est. diameter</div>
              </InfoHeader>
              <InfoContent>
                {fmtDiameter(asteroid.estimated_diameter)}
              </InfoContent>
            </InfoBox>
          </Grid>
        </Main>
      )}
    </div>
  );
}

const StyledLoading = styled(Loading)`
  margin: 40px auto;
`;

const InfoBox = styled.div`
  display: contents;
`;

const InfoHeader = styled.h3`
  font-weight: normal;
  margin: 0;
  background-color: var(--accent);
  color: var(--dark);
  padding: 15px;
  font-size: 20px;
  text-transform: uppercase;
  letter-spacing: 1px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
  gap: 10px;

  .bx {
    font-size: 60px;
  }
`;

const InfoContent = styled.p`
  margin: 0;
  font-size: 40px;
  padding: 20px;
  background-color: white;
  color: var(--dark);
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const Main = styled.main`
  max-width: 700px;
  padding: 30px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 30px;
`;

const Grid = styled.div`
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 20px 0;
`;

const Header = styled.header`
  background-color: white;
  color: var(--dark);
  display: flex;
  gap: 20px;
  align-items: center;
`;

const HomeLink = styled.a`
  background-color: var(--mid-accent);
  text-decoration: none;
  padding: 20px;
  display: flex;
  color: white;
  gap: 10px;
  font-size: 20px;
  align-items: center;
  padding-right: 30px;
  width: 80px;
  height: 80px;
  position: relative;
  transition: background-color 0.3s;
  font-weight: bold;

  ::after {
    content: "";
    display: block;
    width: 90px;
    height: 80px;
    background-color: var(--mid-accent);
    position: absolute;
    top: 0;
    left: calc(100% - 1px);
    transform: translateX(-100%);
    transition: transform 0.5s, background-color 0.3s;
  }

  & > * {
    position: relative;
    z-index: 1;
    white-space: nowrap;
    pointer-events: none;
  }

  .title-text {
    opacity: 0;
    transition: opacity 0.5s;
  }

  :hover {
    background-color: var(--mid-accent);
    ::after {
      transform: translateX(0);
    }

    .title-text {
      opacity: 1;
    }
  }

  :hover:active {
    background-color: var(--dark);

    ::after {
      background-color: var(--dark);
    }
  }

  .bx-meteor {
    font-size: 35px;
    color: white;
  }
`;

const H1 = styled.h1`
  color: var(--dark-accent);
  margin: 0;
`;

const Status = styled.div`
  padding: 10px 20px;
  border-radius: 5px;
  text-align: center;
  font-size: 30px;
  color: var(--text);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border: 3px solid var(--border);
  background-color: var(--bg);
  width: fit-content;
  margin: 0 auto;

  & svg {
    background-color: var(--icon-bg);
    color: var(--icon-fg);
    width: 50px;
    height: 50px;
    border-radius: 25px;
    padding: 5px;
  }
`;

const DangerStatus = styled(Status)`
  --text: var(--dark);
  --border: var(--red);
  --bg: var(--red);
  --icon-bg: var(--red);
  --icon-fg: var(--dark);
`;

const SafeStatus = styled(Status)`
  --text: var(--accent);
  --border: var(--dark-accent);
  --bg: var(--very-dark-accent);
  --icon-fg: var(--dark-accent);
  --icon-bg: var(--accent);
`;

const ErrorMessage = styled.div`
  color: var(--red);
  text-align: center;
  margin-top: 100px;
  font-size: 30px;

  svg {
    font-size: 150px;
  }
`;

const Button = styled.button`
  background-color: var(--dark-accent);
  font-size: 20px;
  border-radius: 5px;
  padding: 10px 20px;
  color: var(--dark);
  font-weight: bold;
  margin-top: 40px;
  border: none;
  cursor: pointer;
`;
