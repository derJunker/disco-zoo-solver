const discoZooLocalStoragePrefix = 'disco-zoo_';

function getDiscoZooLocalStorageKey(key: string) {
  return discoZooLocalStoragePrefix + key;
}

export function getDiscoZooLocalStorageItem(key: string) {
  return localStorage.getItem(getDiscoZooLocalStorageKey(key));
}

export function setDiscoZooLocalStorageItem(key: string, value: string) {
  localStorage.setItem(getDiscoZooLocalStorageKey(key), value);
}

export function removeDiscoZooLocalStorageItem(key: string) {
  localStorage.removeItem(getDiscoZooLocalStorageKey(key));
}