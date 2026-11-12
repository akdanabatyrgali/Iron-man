// Web interface only: the server (Java) owns all gameplay rules and state.
// JavaScript sends requests and displays the JSON returned by Spring Boot.

const byId = (id) => document.getElementById(id);

async function request(path, method = "GET", body = null) {
  const options = { method, headers: { "Accept": "application/json" } };
  if (body !== null) {
    options.headers["Content-Type"] = "application/json";
    options.body = JSON.stringify(body);
  }
  const response = await fetch(path, options);
  const json = await response.json();
  if (!response.ok) throw new Error(json.detail || json.message || "Server error");
  return json;
}

function fillSelect(id, entries) {
  const select = byId(id);
  select.replaceChildren();
  entries.forEach((item) => {
    const option = document.createElement("option");
    option.value = item.id;
    option.textContent = item.name;
    select.appendChild(option);
  });
}

function showDashboard(dashboard) {
  const suit = dashboard.state;
  byId("currentSuit").textContent = suit.suitName || "Not selected";
  byId("currentState").textContent = suit.state || "—";
  byId("assistant").textContent = suit.assistant || "—";
  byId("currentWeapon").textContent = suit.weaponName || "—";
  byId("energyText").textContent = suit.selected ? `${suit.energy} / ${suit.maxEnergy}` : "—";
  byId("armorText").textContent = suit.selected ? `${suit.armor} / ${suit.maxArmor}` : "—";
  byId("energyBar").max = suit.maxEnergy || 100;
  byId("energyBar").value = suit.energy;
  byId("armorBar").max = suit.maxArmor || 100;
  byId("armorBar").value = suit.armor;
  byId("missionStatus").textContent = suit.inMission
    ? `${suit.missionName} — ${suit.enemyName}: ${suit.enemyHealth} / ${suit.enemyMaxHealth} HP`
    : "No active mission.";

  const history = byId("historyList");
  history.replaceChildren();
  if (dashboard.history.length === 0) {
    history.textContent = "No missions completed yet.";
  } else {
    dashboard.history.forEach((record) => {
      const item = document.createElement("div");
      item.className = "history-item";
      const name = document.createElement("strong");
      name.textContent = record.missionName;
      const result = document.createElement("span");
      result.textContent = `${record.suitName} · ${record.success ? "SUCCESS" : "FAILED"}`;
      item.append(name, result);
      history.appendChild(item);
    });
  }
}

function showMessage(message, success) {
  byId("actionMessage").textContent = message;
  byId("actionMessage").parentElement.classList.toggle("error", !success);
}

async function send(path, value = null) {
  document.querySelectorAll("button").forEach((button) => button.disabled = true);
  try {
    const answer = await request(path, "POST", value === null ? null : { value });
    showDashboard(answer.dashboard);
    showMessage(answer.message, answer.success);
  } catch (error) {
    showMessage(error.message, false);
  } finally {
    document.querySelectorAll("button").forEach((button) => button.disabled = false);
  }
}

async function start() {
  try {
    const catalog = await request("/api/catalog");
    fillSelect("suitSelect", catalog.suits);
    fillSelect("weaponSelect", catalog.weapons);
    fillSelect("missionSelect", catalog.missions);
    showDashboard(await request("/api/dashboard"));
    byId("serverStatus").textContent = "Java server connected";
    byId("serverStatus").classList.add("connected");
  } catch (error) {
    byId("serverStatus").textContent = "Java server not connected";
    byId("serverStatus").classList.add("disconnected");
    showMessage("Start StarkApplication.java, then open http://localhost:8080/", false);
  }
}

byId("selectSuitBtn").addEventListener("click", () => send("/api/suits", byId("suitSelect").value));
byId("selectWeaponBtn").addEventListener("click", () => send("/api/weapons", byId("weaponSelect").value));
byId("startMissionBtn").addEventListener("click", () => send("/api/missions", byId("missionSelect").value));
byId("actionButtons").addEventListener("click", (event) => {
  if (event.target.dataset.action) send(`/api/actions/${event.target.dataset.action}`);
});

start();
