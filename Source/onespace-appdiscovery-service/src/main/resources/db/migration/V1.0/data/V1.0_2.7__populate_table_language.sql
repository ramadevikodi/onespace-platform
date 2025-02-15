BEGIN;
DO $$
BEGIN
    INSERT INTO language(language_code, description, date_format, time_format, currency_format)
    values ('pt-PT', 'Portuguese (European)', '', '', ''),
           ('de-DE', 'Deutsch (Germany)', '', '', ''),
           ('en-US', 'English (United States)', '', '', ''),
           ('nl-NL', 'Dutch (Netherlands)', '', '', ''),
           ('fr-FR', 'French (France)', '', '', ''),
           ('it-IT', 'Italian (Italy)', '', '', ''),
           ('ja-JP', 'Japanese (Japan)', '', '', ''),
           ('es-ES', 'Spanish (Spain)', '', '', ''),
           ('N/A', 'Not applicable', '', '', ''),
           ('af_ZA', 'Afrikaans (South Africa)', 'YYYY-MM-DD', 'h:mm', ''),
           ('am_ET', 'Amharic (Ethiopia)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_AE', 'Arabic (United Arab Emirates)', 'DD/MM/YYYY', 'h:mm', ''),
           ('ar_BH', 'Arabic (Bahrain)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_DZ', 'Arabic (Algeria)', 'DDYYYY/MM/', 'h:mm', ''),
           ('ar_EG', 'Arabic (Egypt)', 'YYYY/M/DD', 'h:mm', ''),
           ('ar_IQ', 'Arabic (Iraq)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_JO', 'Arabic (Jordan)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_KW', 'Arabic (Kuwait)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_LB', 'Arabic (Lebanon)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_LY', 'Arabic (Libya)', 'DDYYYY/MM/', 'h:mm', ''),
           ('ar_MA', 'Arabic (Morocco)', 'YYYY/M/DD', 'h:mm', ''),
           ('ar_OM', 'Arabic (Oman)', 'YYYY/M/DD', 'h:mm', ''),
           ('ar_QA', 'Arabic (Qatar)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_SA', 'Arabic (Saudi Arabia)', 'dd/mm/yyyy', 'h:mm', ''),
           ('ar_SD', 'Arabic (Sudan)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_SY', 'Arabic (Syria)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ar_TN', 'Arabic (Tunisia)', 'DDYYYY/MM/', 'h:mm', ''),
           ('ar_YE', 'Arabic (Yemen)', 'YYYY/M/DD', 'h:mm', ''),
           ('az_AZ', 'Azerbaijani (Azerbaijan)', 'DD.MM.YYYY', 'h:mm', ''),
           ('be_BY', 'Belarusian (Belarus)', 'DD.MM.YYYY', 'h:mm', ''),
           ('bg_BG', 'Bulgarian (Bulgaria)', 'DD.MM.YYYY', 'h:mm', ''),
           ('bn_BD', 'Bangla (Bangladesh)', 'DD.MM.YYYY', 'h:mm', ''),
           ('bn_IN', 'Bangla (India)', 'DD/M/YYYY', 'h:mm', ''),
           ('bs_BA', 'Bosnian (Bosnia & Herzegovina)', 'DD/M/YYYY', 'h:mm', ''),
           ('ca_ES', 'Catalan (Spain)', 'DD. M.YYYY', 'h:mm', ''),
           ('ca_ES_EURO', 'Catalan (Spain,Euro)', 'DD/M/YYYY', 'h:mm', ''),
           ('cs_CZ', 'Czech (Czechia)', 'DD/M/YYYY', 'h:mm', ''),
           ('cy_GB', 'Welsh (United Kingdom)', 'DD.MM.YYYY', 'h:mm', ''),
           ('da_DK', 'Danish (Denmark)', 'DD/MM/YYYY', 'h:mm', ''),
           ('de_AT', 'German (Austria)', 'DD.MM.YYYY', 'h:mm', ''),
           ('de_AT_EURO', 'German (Austria,Euro)', 'DD.MM.YYYY', 'h:mm', ''),
           ('de_BE', 'German (Belgium)', 'DD.MM.YYYY', 'h:mm', ''),
           ('de_CH', 'German (Switzerland)', 'DD.MM.YYYY', 'h:mm', ''),
           ('de_DE', 'German (Germany)', 'DD.MM.YYYY', 'h:mm', ''),
           ('de_DE_EURO', 'German (Germany,Euro)', 'DD.MM.YYYY', 'h:mm', ''),
           ('de_LU', 'German (Luxembourg)', 'DD.MM.YYYY', 'h:mm', ''),
           ('de_LU_EURO', 'German (Luxembourg,Euro)', 'DD.MM.YYYY', 'h:mm', ''),
           ('dz_BT', 'Dzongkha (Bhutan)', 'DD.MM.YYYY', 'h:mm', ''),
           ('el_GR', 'Greek (Greece)', 'DD/M/YYYY', 'h:mm', ''),
           ('en_AG', 'English (Antigua & Barbuda)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_AU', 'English (Australia)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_BB', 'English (Barbados)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_BM', 'English (Bermuda)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_BS', 'English (Bahamas)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_BW', 'English (Botswana)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_BZ', 'English (Belize)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_CA', 'English (Canada)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_CM', 'English (Cameroon)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_ER', 'English (Eritrea)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_FJ', 'English (Fiji)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_FK', 'English (Falkland Islands)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_GB', 'English (United Kingdom)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_GH', 'English (Ghana)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_GI', 'English (Gibraltar)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_GM', 'English (Gambia)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_GY', 'English (Guyana)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_HK', 'English (Hong Kong SAR China)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_ID', 'English (Indonesia)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_IE', 'English (Ireland)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_IE_EURO', 'English (Ireland,Euro)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_IN', 'English (India)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_JM', 'English (Jamaica)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_KE', 'English (Kenya)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_KY', 'English (Cayman Islands)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_LR', 'English (Liberia)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_MG', 'English (Madagascar)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_MU', 'English (Mauritius)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_MW', 'English (Malawi)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_MY', 'English (Malaysia)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_NA', 'English (Namibia)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_NG', 'English (Nigeria)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_NZ', 'English (New Zealand)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_PG', 'English (Papua New Guinea)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_PH', 'English (Philippines)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_PK', 'English (Pakistan)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_RW', 'English (Rwanda)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_SB', 'English (Solomon Islands)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_SC', 'English (Seychelles)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_SG', 'English (Singapore)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_SH', 'English (St. Helena)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_SL', 'English (Sierra Leone)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_SX', 'English (Sint Maarten)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_SZ', 'English (Eswatini)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_TO', 'English (Tonga)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_TT', 'English (Trinidad & Tobago)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_TZ', 'English (Tanzania)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_UG', 'English (Uganda)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_US', 'English (United States)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_VU', 'English (Vanuatu)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_WS', 'English (Samoa)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('en_ZA', 'English (South Africa)', 'DD-MMM-YYYY', 'h:mm', ''),
           ('es_AR', 'Spanish (Argentina)', 'YYYY/MM/DD', 'h:mm', ''),
           ('es_BO', 'Spanish (Bolivia)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_CL', 'Spanish (Chile)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_CO', 'Spanish (Colombia)', 'DD-MM-YYYY', 'h:mm', ''),
           ('es_CR', 'Spanish (Costa Rica)', 'DD/MM/YYYY', 'h:mm', ''),
           ('es_CU', 'Spanish (Cuba)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_DO', 'Spanish (Dominican Republic)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_EC', 'Spanish (Ecuador)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_ES', 'Spanish (Spain)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_ES_EURO', 'Spanish (Spain,Euro)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_GT', 'Spanish (Guatemala)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_HN', 'Spanish (Honduras)', 'DD/MM/YYYY', 'h:mm', ''),
           ('es_MX', 'Spanish (Mexico)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_NI', 'Spanish (Nicaragua)', 'DD/MM/YYYY', 'h:mm', ''),
           ('es_PA', 'Spanish (Panama)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_PE', 'Spanish (Peru)', 'MM/DD/YYYY', 'h:mm', ''),
           ('es_PR', 'Spanish (Puerto Rico)', 'DD/MM/YYYY', 'h:mm', ''),
           ('es_PY', 'Spanish (Paraguay)', 'MM/DD/YYYY', 'h:mm', ''),
           ('es_SV', 'Spanish (El Salvador)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_US', 'Spanish (United States)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_UY', 'Spanish (Uruguay)', 'DD/M/YYYY', 'h:mm', ''),
           ('es_VE', 'Spanish (Venezuela)', 'DD/M/YYYY', 'h:mm', ''),
           ('et_EE', 'Estonian (Estonia)', 'DD/M/YYYY', 'h:mm', ''),
           ('eu_ES', 'Basque (Spain)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fa_IR', 'Persian (Iran)', 'M/DD/YYYY', 'h:mm', ''),
           ('fi_FI', 'Finnish (Finland)', 'DD.MM.YYYY', 'h:mm', ''),
           ('fi_FI_EURO', 'Finnish (Finland,Euro)', 'DD/M/YYYY', 'h:mm', ''),
           ('fr_BE', 'French (Belgium)', 'DD/M/YYYY', 'h:mm', ''),
           ('fr_CA', 'French (Canada)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fr_CH', 'French (Switzerland)', 'YYYY-MM-DD', 'h:mm', ''),
           ('fr_FR', 'French (France)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fr_FR_EURO', 'French (France,Euro)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fr_GN', 'French (Guinea)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fr_HT', 'French (Haiti)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fr_LU', 'French (Luxembourg)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fr_LU_EURO', 'French (Luxembourg,Euro)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fr_ML', 'French (Mali)', 'DD/MM/YYYY', 'h:mm', ''),
           ('fr_SN', 'French (Senegal)', 'DD/MM/YYYY', 'h:mm', ''),
           ('ga_IE', 'Irish (Ireland)', 'DD-MM-YYYY', 'h:mm', ''),
           ('ga_IE_EURO', 'Irish (Ireland,Euro)', 'DD-MM-YYYY', 'h:mm', ''),
           ('gu_IN', 'Gujarati (India)', 'DD-MM-YYYY', 'h:mm', ''),
           ('he_IL', 'Hebrew (Israel)', 'DD.MM.YYYY', 'h:mm', ''),
           ('hi_IN', 'Hindi (India)', 'DD-MM-YYYY', 'h:mm', ''),
           ('hr_HR', 'Croatian (Croatia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('hu_HU', 'Hungarian (Hungary)', 'YYYY-MM-DD', 'h:mm', ''),
           ('hy_AM', 'Armenian (Armenia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('id_ID', 'Indonesian (Indonesia)', 'DD/MM/YYYY', 'h:mm', ''),
           ('is_IS', 'Icelandic (Iceland)', 'YYYY-MM-DD', 'h:mm', ''),
           ('it_CH', 'Italian (Switzerland)', 'DD/MM/YYYY', 'h:mm', ''),
           ('it_IT', 'Italian (Italy)', 'DD/MM/YYYY', 'h:mm', ''),
           ('it_IT_EURO', 'Italian (Italy,Euro)', 'DD/MM/YYYY', 'h:mm', ''),
           ('ja_JP', 'Japanese (Japan)', 'YYYY/MM/DD', 'h:mm', ''),
           ('ka_GE', 'Georgian (Georgia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('kk_KZ', 'Kazakh (Kazakhstan)', 'DD.MM.YYYY', 'h:mm', ''),
           ('km_KH', 'Khmer (Cambodia)', 'DD-MM-YYYY', 'h:mm', ''),
           ('kn_IN', 'Kannada (India)', 'DD-MM-YYYY', 'h:mm', ''),
           ('ko_KR', 'Korean (South Korea)', 'YYYY.MM.DD', 'h:mm', ''),
           ('ky_KG', 'Kyrgyz (Kyrgyzstan)', 'DD/MM/YYYY', 'h:mm', ''),
           ('lo_LA', 'Lao (Laos)', 'DD/MM/YYYY', 'h:mm', ''),
           ('lt_LT', 'Lithuanian (Lithuania)', 'DD/MM/YYYY', 'h:mm', ''),
           ('lv_LV', 'Latvian (Latvia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('mk_MK', 'Macedonian (North Macedonia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('ml_IN', 'Malayalam (India)', 'DD/MM/YYYY', 'h:mm', ''),
           ('mn_MN', 'Mongolian (Mongolia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('mr_IN', 'Marathi (India)', 'DD/MM/YYYY', 'h:mm', ''),
           ('ms_BN', 'Malay (Brunei)', 'DD-MM-YYYY', 'h:mm', ''),
           ('ms_MY', 'Malay (Malaysia)', 'DD-MM-YYYY', 'h:mm', ''),
           ('mt_MT', 'Maltese (Malta)', 'DD-MM-YYYY', 'h:mm', ''),
           ('my_mm', 'Burmese (Myanmar)', 'DD-MM-YYYY', 'h:mm', ''),
           ('nb_NO', 'Norwegian Bokmål (Norway)', 'DD.MM.YYYY', 'h:mm', ''),
           ('ne_NP', 'Nepali (Nepal)', 'DD-MM-YYYY', 'h:mm', ''),
           ('nl_BE', 'Dutch (Belgium)', 'DD-MM-YYYY', 'h:mm', ''),
           ('nl_NL', 'Dutch (Netherlands)', 'DD-MM-YYYY', 'h:mm', ''),
           ('nl_NL_EURO', 'Dutch (Netherlands,Euro)', 'DD-MM-YYYY', 'h:mm', ''),
           ('no_NO', 'Norwegian (Norway)', 'DD-MM-YYYY', 'h:mm', ''),
           ('or_IN', 'Odia (India)', 'DD-MM-YYYY', 'h:mm', ''),
           ('pa_IN', 'Punjabi (India)', 'DD-MM-YYYY', 'h:mm', ''),
           ('pl_PL', 'Polish (Poland)', 'DD.MM.YYYY', 'h:mm', ''),
           ('pt_BR', 'Portuguese (Brazil)', 'DD/MM/YYYY', 'h:mm', ''),
           ('pt_PT', 'Portuguese (Portugal)', 'DD/MM/YYYY', 'h:mm', ''),
           ('pt_PT_EURO', 'Portuguese (Portugal,Euro)', 'DD/MM/YYYY', 'h:mm', ''),
           ('ro_MD', 'Romanian (Moldova)', 'DD.MM.YYYY', 'h:mm', ''),
           ('ro_RO', 'Romanian (Romania)', 'DD.MM.YYYY', 'h:mm', ''),
           ('ru_RU', 'Russian (Russia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('ru_UA', 'Russian (Ukraine)', 'DD.MM.YYYY', 'h:mm', ''),
           ('si_LK', 'Sinhala (Sri Lanka)', 'DD/MM/YYYY', 'h:mm', ''),
           ('sk_SK', 'Slovak (Slovakia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('sl_SI', 'Slovenian (Slovenia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('sq_AL', 'Albanian (Albania)', 'DD.MM.YYYY', 'h:mm', ''),
           ('sr_RS', 'Serbian (Serbia)', 'DD.MM.YYYY', 'h:mm', ''),
           ('sv_SE', 'Swedish (Sweden)', 'YYYY-MM-DD', 'h:mm', ''),
           ('sw_KE', 'Swahili (Kenya)', 'DD/MM/YYYY', 'h:mm', ''),
           ('ta_IN', 'Tamil (India)', 'DD/MM/YYYY', 'h:mm', ''),
           ('te_IN', 'Telugu (India)', 'DD/MM/YYYY', 'h:mm', ''),
           ('th_TH', 'Thai (Thailand)', 'DD/MM/YYYY', 'h:mm', ''),
           ('tl_PH', 'Filipino (Philippines)', 'DD/MM/YYYY', 'h:mm', ''),
           ('tr_TR', 'Turkish (Turkey)', 'DD/MM/YYYY', 'h:mm', ''),
           ('uk_UA', 'Ukrainian (Ukraine)', 'DD.MM.YYYY', 'h:mm', ''),
           ('ur_PK', 'Urdu (Pakistan)', 'DD/MM/YYYY', 'h:mm', ''),
           ('uz_UZ', 'Uzbek (Uzbekistan)', 'DD/MM/YYYY', 'h:mm', ''),
           ('vi_VN', 'Vietnamese (Vietnam)', 'DD/MM/YYYY', 'h:mm', ''),
           ('zh_CN', 'Chinese (China)', 'YYYY-MM-DD', 'h:mm', ''),
           ('zh_HK', 'Chinese (Hong Kong SAR China)', 'YYYY-MM-DD', 'h:mm', ''),
           ('zh_SG', 'Chinese (Singapore)', 'YYYY-MM-DD', 'h:mm', ''),
           ('zh_TW', 'Chinese (Taiwan)', 'YYYY-MM-DD', 'h:mm', ''),
           ('zu_ZA', 'Zulu (South Africa)', 'DD/MM/YYYY', 'h:mm', '');


    RAISE NOTICE 'Data inserted successfully.';
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE EXCEPTION 'An error occurred during the insertion. Transaction has been rolled back: %', SQLERRM;
END $$;
COMMIT;