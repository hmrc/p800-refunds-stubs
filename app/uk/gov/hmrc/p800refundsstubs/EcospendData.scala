/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.p800refundsstubs

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.p800refundsstubs.models.EcospendErrorResponse

object EcospendData {

  val invalidAccessTokenCheckResponse: EcospendErrorResponse = EcospendErrorResponse(
    "INVALID_ACCESS_TOKEN",
    "Access token provided in Authorization header was not valid",
    Json.obj()
  )

  val missingAccessTokenCheckResponse: EcospendErrorResponse = EcospendErrorResponse(
    "MISSING_ACCESS_TOKEN",
    "No Authorization header was present so access token couldn't be retrieved",
    Json.obj()
  )

  val badRequestErrorReponse: EcospendErrorResponse = EcospendErrorResponse(
    "BadRequest",
    "Bad Request 400",
    Json.obj()
  )

  val internalServerErrorResponse: EcospendErrorResponse = EcospendErrorResponse(
    "InternalServerError",
    "Internal Server Error 500",
    Json.obj()
  )

  val badGatewayErrorResponse: EcospendErrorResponse = EcospendErrorResponse(
    "BadGateway",
    "Bad Gateway 502",
    Json.obj()
  )

  val serviceUnavailableErrorResponse: EcospendErrorResponse = EcospendErrorResponse(
    "ServiceUnavailable",
    "Service Unavailable 503",
    Json.obj()
  )

  //language=JSON
  val getBanksResponseJson: JsValue = Json.parse(
    s"""{
          "data": [
            {
              "bank_id": "obie-barclays-personal",
              "name": "Barclays Personal",
              "friendly_name": "Barclays Personal",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Barclays_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Barclays",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-barclays-business",
              "name": "Barclays Business",
              "friendly_name": "Barclays Business",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Barclays_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Barclays",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-bad-request",
              "name": "Test | Bad Request",
              "friendly_name": "Test | Bad Request",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-unauthorized-401",
              "name": "Test | Unauthorized 401",
              "friendly_name": "Test | Unauthorized 401",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-server-error-500",
              "name": "Test | Internal Server Error 500",
              "friendly_name": "Test | Internal Server Error 500",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-server-error-502",
              "name": "Test | Bad Gateway 502",
              "friendly_name": "Test | Bad Gateway 502",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-server-error-503",
              "name": "Test | Service Unavailable 503",
              "friendly_name": "Test | Service Unavailable 503",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-bank-id-none",
              "name": "Test | Account Summary | BankID set to None",
              "friendly_name": "Test | Account Summary | BankID set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-account-identification-none",
              "name": "Test | Account Summary | Account Identification set to None",
              "friendly_name": "Test | Account Summary | Account Identification set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-calculated-owner-name-none",
              "name": "Test | Account Summary | Calculated owner name set to None",
              "friendly_name": "Test | Account Summary | Calculated owner name set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-display-name-none",
              "name": "Test | Account Summary | Display name set to None",
              "friendly_name": "Test | Account Summary | Display name set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-parties-none",
              "name": "Test | Account Summary | Parties set to None",
              "friendly_name": "Test | Account Summary | Parties set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-parties-name-none",
              "name": "Test | Account Summary | Parties name set to None",
              "friendly_name": "Test | Account Summary | Parties name set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-parties-full-legal-name-none",
              "name": "Test | Account Summary | Parties 'full legal name' set to None",
              "friendly_name": "Test | Account Summary | Parties 'full legal name' set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-parties-all-none",
              "name": "Test | Account Summary | Parties all names set to None",
              "friendly_name": "Test | Account Summary | Parties all names set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "test-account-summary-all-none",
              "name": "Test | Account Summary | All optional fields set to None",
              "friendly_name": "Test | Account Summary | All optional fields set to None",
              "is_sandbox": true,
              "logo": "https://logo.com",
              "icon": "https://example.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Test",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-lloyds-personal",
              "name": "Lloyds Personal",
              "friendly_name": "Lloyds Personal",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Lloyds_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Lloyds",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-lloyds-business",
              "name": "Lloyds Business",
              "friendly_name": "Lloyds Business",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Lloyds_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Lloyds",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-monzo-personal",
              "name": "Monzo Personal",
              "friendly_name": "Monzo Personal",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Monzo_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Monzo",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-monzo-business",
              "name": "Monzo Business",
              "friendly_name": "Monzo Business",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Monzo_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Monzo",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-monzo-special",
              "name": "Monzo Special",
              "friendly_name": "Monzo Special",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Monzo_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Monzo",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-natwest-personal",
              "name": "Natwest Personal",
              "friendly_name": "Natwest Personal",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/NatWest_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Natwest",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-natwest-business",
              "name": "Natwest Business",
              "friendly_name": "Natwest Business",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/NatWest_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Natwest",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-natwest-online-and-mobile",
              "name": "Natwest Online",
              "friendly_name": "NatWest Online and Mobile Banking&.-",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/NatWest_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Natwest",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-hsbc-personal",
              "name": "HSBC Personal",
              "friendly_name": "HSBC Personal",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/HSBC_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "HSBC",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-hsbc-business",
              "name": "HSBC Business",
              "friendly_name": "HSBC Business",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/HSBC_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "HSBC",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-santander-personal",
              "name": "Santander Personal",
              "friendly_name": "Santander Personal",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Santander_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Santander",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-santander-business",
              "name": "Santander Business",
              "friendly_name": "Santander Business",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/Santander_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Santander",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-chase",
              "name": "Chase",
              "friendly_name": "Chase",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://icon.com",
              "standard": "obie",
              "country_iso_code": "",
              "group": "Chase Bank",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-z-bank",
              "name": "Z Bank Business",
              "friendly_name": "Z Bank Business",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://icon.com",
              "standard": "obie",
              "country_iso_code": "",
              "group": "Z Bank",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-rbs-personal",
              "name": "RBS Personal",
              "friendly_name": "RBS Personal",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/RBS_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "RBS",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-rbs-business",
              "name": "RBS Business",
              "friendly_name": "RBS Business",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/RBS_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "RBS",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-rbs-bankline",
              "name": "RBS Bankline",
              "friendly_name": "RBS Bankline",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://public.ecospend.com/images/banks/RBS_icon.svg",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "RBS",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "obie-m&s",
              "name": "M&S Bank",
              "friendly_name": "M&S Bank",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://icon.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "M&S Bank",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": true
            },
            {
              "bank_id": "unavailable-bank",
              "name": "Unavailable Bank",
              "friendly_name": "Unavailable Bank",
              "is_sandbox": false,
              "logo": "https://logo.com",
              "icon": "https://icon.com",
              "standard": "obie",
              "country_iso_code": "",
              "division": "GB",
              "group": "Unavailable Bank",
              "order": 0,
              "abilities": {
                "account": true,
                "balance": true,
                "transactions": true,
                "direct_debits": true,
                "standing_orders": true,
                "parties": true,
                "scheduled_payments": true,
                "statements": true,
                "offers": true
              },
              "service_status": false
            }
          ],
          "meta": {
            "total_count": 0,
            "total_pages": 0,
            "current_page": 0
          }
        }""".stripMargin
  )
}

