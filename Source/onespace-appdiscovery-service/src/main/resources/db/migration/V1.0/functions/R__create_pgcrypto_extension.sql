DO $$
BEGIN
  IF NOT EXISTS (
     SELECT 1
     FROM pg_extension
     WHERE extname = 'pgcrypto'
  ) THEN
      CREATE EXTENSION pgcrypto;
  END IF;
END $$;
