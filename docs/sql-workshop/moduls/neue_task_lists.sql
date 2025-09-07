INSERT INTO task_lists (
  id,
  created,
  updated,
  title,
  description
) VALUES (
  gen_random_uuid(),                      -- UUID generieren
  now(),                                  -- created timestamp
  now(),                                  -- updated timestamp
  '🧪 Testliste ohne Tasks',              -- Titel
  'Diese Liste enthält keine Tasks.'     -- Beschreibung
);