BEGIN;
DO $$
BEGIN

UPDATE business_unit
SET business_unit_name  = 'Engineering'
WHERE "cluster" = 'Innovation  & Strategy';

UPDATE business_unit
SET description = 'Organization responsible for developing and maintaining OneSpace Platform'
WHERE business_unit_name = 'Engineering';

UPDATE business_unit
SET description = 'Delivers echography solutions focused on diagnosis, treatment planning and guidance for cardiology, general imaging, obstetrics/gynecology, and point-of-care applications, as well as proprietary AI-enabled and intelligent software capabilities to enable early, advanced diagnostics and timely interventions, and remote capabilities to enable tele-ultrasound operations and training.'
WHERE business_unit_name = 'Ultrasound';

UPDATE business_unit
SET description = 'Delivers systems with associated software to optimize diagnostic imaging quality and improve efficiency and productivity for the hospital.'
WHERE business_unit_name = 'DXR';

UPDATE business_unit
SET description = 'Delivers systems with helium-free-for-life operations, bundled with associated AI-enabled software to streamline workflows, optimize diagnostic quality, and improve patient experience.'
WHERE business_unit_name = 'MRI';

UPDATE business_unit
SET description = 'Delivers advanced and efficient systems and software, including detector-based Spectral CT and molecular and hybrid imaging solutions for nuclear medicine.'
WHERE business_unit_name = 'CT';

UPDATE business_unit
SET description = 'Delivers integrated interventional systems that combine information from imaging systems, interventional devices, navigation tools, monitoring patient data and patient health records, supported by AI, to provide interventional staff with the control and information they need to perform procedures efficiently.'
WHERE business_unit_name = 'Systems';

UPDATE business_unit
SET description = 'Delivers interventional diagnostic and therapeutic devices to treat coronary artery and peripheral vascular disease.'
WHERE business_unit_name = 'Devices';

UPDATE business_unit
SET description = 'Delivers acute patient management solutions to improve clinical and patient outcomes and achieve operational and economic efficiencies. Leveraging a strong presence in the operating theater and intensive care unit (ICU), Hospital Patient Monitoring solutions enhance customers’ experience and improve patient outcomes with seamless patient data pulled from over 1,000 vendor-neutral devices monitoring from admission to discharge, and by turning that patient data into clinical insights that are actionable at the right time and specific to targeted care settings.'
WHERE business_unit_name = 'Hospital Patient Monitoring (HPM)';

UPDATE business_unit
SET description = 'Provides patient care management in ambulatory and home care settings through a suite of cardiac diagnostic and monitoring solutions to identify heart rhythm disorders plus other disease states supported by AI algorithms that orchestrate workflows and services across care settings to provide care virtually anywhere.'
WHERE business_unit_name = 'Ambulatory Monitoring and Diagnostics (AM&D)';

UPDATE business_unit
SET description = 'Propositions offered by EC play a critical role in connected acute care management, both inside and outside the hospital, including cardiac resuscitation (e.g. Automated External Defibrillators) and emergency care solutions (devices, services, and digital/data solutions) for professional and consumer applications.'
WHERE business_unit_name = 'Emergency Care';

UPDATE business_unit
SET description = 'The EMR & Care Management business unit provides electronic medical record (EMR), acute care, and TeleICU solutions to deliver intelligent informatics propositions to connect people, data, and technology across care settings.'
WHERE business_unit_name = 'EMR & CM';

UPDATE business_unit
SET description = 'The Cardiovascular Informatics business unit offers solutions that empower caregivers across the enterprise to make fast, informed clinical decisions to care for cardiac patients, providing support in managing increased volume and complexity of clinical and administrative data, wherever care is delivered.'
WHERE business_unit_name = 'Cardiovascular Informatics';

UPDATE business_unit
SET description = 'Offers medical device and data integration across the enterprise for continuous, vendor-neutral data capture from more than 1,000 device models supported by insightful clinical decision support and analysis.'
WHERE business_unit_name = 'Clinical Integration and Insights';

UPDATE business_unit
SET description = 'Delivers clinical decision support in the domains of digital pathology, advanced visualization and specific disease management solutions.'
WHERE business_unit_name = 'Clinical Insights and Informatics (CDS)';

UPDATE business_unit
SET description = 'Offers power toothbrushes for a range of price segments, from entry-level battery-operated toothbrushes for a young audience to premium power toothbrushes connected to the Sonicare app with in-app coaching; brush heads, which are also available as a subscription service; products for interdental cleaning and for teeth whitening.'
WHERE business_unit_name = 'Oral Healthcare';

UPDATE business_unit
SET description = 'Offers products to support parents and babies in the first 1,000 days, including infant feeding (breast pumps, baby bottles and sterilizers), connected baby monitors and digital parental and women’s health solutions (Pregnancy+ and Baby+ apps).'
WHERE business_unit_name = 'Mother and Child';

UPDATE business_unit
SET description = 'Offers grooming and beauty products ranging from entry-level to premium. The grooming portfolio includes shavers, OneBlade, groomers, trimmers and hair clippers, as well as premium solutions with SkinIQ technology, in-app coaching for a personalized shave, and blade subscriptions. The beauty portfolio includes devices to support skin care, hair care and hair removal, including Lumea premium IPL hair removal devices and solutions with the latest SenseIQ technology that sense and adapt for personalized care; also available through subscription models.'
WHERE business_unit_name = 'Personal Care';

     RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;