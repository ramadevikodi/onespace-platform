-- Script: Insert data into the market table
BEGIN;
DO $$
BEGIN
    -- Insert into market
    INSERT INTO market (market_name, region, country, sub_region)
    values ('NAM', 'NAM', 'United States', 'ALL'),
           ('NAM', 'NAM', 'Canada', 'ALL'),
           ('EU', 'BNL', 'Luxembourg', 'ALL'),
           ('EU', 'BNL', 'Belgium', 'ALL'),
           ('EU', 'BNL', 'The Netherlands', 'ALL'),
           ('EU', 'DACH', 'Germany', 'ALL'),
           ('EU', 'DACH', 'Switzerland', 'ALL'),
           ('EU', 'DACH', 'Austria', 'ALL'),
           ('EU', 'FRA', 'France', 'ALL'),
           ('EU', 'IBE', 'Spain', 'ALL'),
           ('EU', 'IBE', 'Portugal', 'ALL'),
           ('EU', 'IIG', 'Italy', 'ALL'),
           ('EU', 'NOR', 'Denmark', 'ALL'),
           ('EU', 'NOR', 'Finland', 'ALL'),
           ('EU', 'NOR', 'Sweden', 'ALL'),
           ('EU', 'NOR', 'Norway', 'ALL'),
           ('EU', 'UKI', 'United Kingdom', 'ALL'),
           ('EU', 'UKI', 'Ireland', 'ALL'),
           ('EU-ALL', 'ALL', 'ALL', 'ALL'),
           ('NAM-ALL', 'ALL', 'ALL', 'ALL'),
           ('APAC-ALL', 'ALL', 'ALL', 'ALL'),
           ('APAC', 'South Asia', 'India', 'Central'),
           ('APAC', 'South Asia', 'India', 'South'),
           ('APAC', 'South Asia', 'India', 'North'),
           ('APAC', 'South Asia', 'India', 'East'),
           ('APAC', 'South Asia', 'India', 'West'),
           ('APAC', 'South Asia', 'SriLanka', 'ALL'),
           ('APAC', 'South East Asia', 'Thailand', 'ALL'),
           ('APAC', 'South East Asia', 'Vietnam', 'ALL'),
           ('APAC', 'North East Asia', 'China', 'ALL'),
           ('APAC', 'North East Asia', 'Russia', 'ALL'),
           ('APAC', 'Central Asia', 'Kazakhstan', 'ALL'),
           ('APAC', 'Pacific', 'Australia', 'ALL'),
           ('APAC', 'South East Asia', 'New Zealand', 'ALL');

    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        -- Rollback the transaction if an error occurs
        ROLLBACK;
        -- Raise an error with a custom message
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
-- Commit the transaction if no errors occur
COMMIT;