BEGIN;
DO $$
BEGIN

INSERT INTO proposition (proposition_name, description)
VALUES ('Not applicable','Not applicable');

UPDATE proposition
SET description = 'Manage fleet and services 24/7 from one hub across modalities, departments and vendors through self service capability, notification center and contract management.'
WHERE proposition_name = 'L0 Service Management pack';

UPDATE proposition
SET description = 'Transparency by equipment log file data for analytics and performance at an enterprise level. Only available for Philips systems & modalities.'
WHERE proposition_name = 'L1 Performance Analytics pack';

UPDATE proposition
SET description = 'Transparency by equipment log file data for insights of asset management.'
WHERE proposition_name = 'L1 Asset Management pack';


     RAISE NOTICE 'Proposition Table Data updated successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the population of Proposition Table. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;