Данный проект представляет собой модуль для поднятия HTTP сервера по работе с SOAP протоколом. 
Проект использует модуль application-http-server и поднимает embeded tomcat в качестве HTTP сервера.

Основные операции модуля:

1) Выдача WSDL и XSD файлов по HTTP протоколу. Для выдачи файлов нужно будет реализовать модуль application-http-file-server, который бы:

* выдавал файлы по айдишнику через HTTP;
* принимал и сохранял файлы по HTTP;

WSDL должна выдаваться по URL вида http://host:port/serviceName?wsdl

2) Обработка HTTP SOAP запросов и вызов метода у сервиса по операции в запросе

Пример WSDL:

```
<?xml version="1.0" encoding="utf-8"?>
<wsdl:definitions xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/" xmlns:wsam="http://www.w3.org/2007/05/addressing/metadata" xmlns:wsx="http://schemas.xmlsoap.org/ws/2004/09/mex" xmlns:wsap="http://schemas.xmlsoap.org/ws/2004/08/addressing/policy" xmlns:msc="http://schemas.microsoft.com/ws/2005/12/wsdl/contract" xmlns:wsp="http://schemas.xmlsoap.org/ws/2004/09/policy" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" xmlns:soap12="http://schemas.xmlsoap.org/wsdl/soap12/" xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:tns="http://tgrad.ru/oss" xmlns:wsa10="http://www.w3.org/2005/08/addressing" xmlns:wsaw="http://www.w3.org/2006/05/addressing/wsdl" xmlns:wsa="http://schemas.xmlsoap.org/ws/2004/08/addressing" name="OrderService" targetNamespace="http://tgrad.ru/oss">
<wsdl:types>
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" elementFormDefault="qualified" targetNamespace="http://tgrad.ru/oss">
<xs:element name="CreateOrder">
<xs:complexType>
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="order" type="tns:Order"/>
</xs:sequence>
</xs:complexType>
</xs:element>
<xs:complexType name="Order">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="SystemName" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="OrderId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="OrderParentId" type="xs:string"/>
<xs:element minOccurs="1" maxOccurs="1" name="OrderDate" nillable="true" type="xs:dateTime"/>
<xs:element minOccurs="0" maxOccurs="1" name="Affiliate" type="xs:string"/>
<xs:element minOccurs="1" maxOccurs="1" name="RetryCount" nillable="true" type="xs:int"/>
<xs:element minOccurs="0" maxOccurs="1" name="SalesChannel" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="TechName" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="SubTechName" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Operator" type="tns:Operator"/>
<xs:element minOccurs="0" maxOccurs="1" name="Address" type="tns:Address"/>
<xs:element minOccurs="0" maxOccurs="1" name="Client" type="tns:Client"/>
<xs:element minOccurs="0" maxOccurs="1" name="Comment" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="OrderActions" type="tns:ArrayOfOrderAction"/>
<xs:element minOccurs="0" maxOccurs="1" name="Attributes" type="tns:ArrayOfAttribute"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="Operator">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="Login" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Name" type="xs:string"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="Address">
<xs:sequence>
<xs:element minOccurs="1" maxOccurs="1" name="Id" nillable="true" type="xs:decimal"/>
<xs:element minOccurs="0" maxOccurs="1" name="HouseId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Flat" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="DepartmentId" type="xs:string"/>
<xs:element minOccurs="1" maxOccurs="1" name="UseFlat" type="xs:boolean"/>
<xs:element minOccurs="0" maxOccurs="1" name="BuildingType" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="BuildingTypeText" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Text" type="xs:string"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="Client">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="Id" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Account" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Name" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Contact" type="xs:string"/>
<xs:element minOccurs="1" maxOccurs="1" name="IsCorp" nillable="true" type="xs:boolean"/>
<xs:element minOccurs="1" maxOccurs="1" name="IsVIP" nillable="true" type="xs:boolean"/>
<xs:element minOccurs="0" maxOccurs="1" name="TypeName" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="HowToCall" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="TimeToCall" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="AdditionalContactInfo" type="tns:ArrayOfAdditionalContact"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="ArrayOfAdditionalContact">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="unbounded" name="AdditionalContact" nillable="true" type="tns:AdditionalContact"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="AdditionalContact">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="Contact" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="HowToCall" type="xs:string"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="ArrayOfOrderAction">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="unbounded" name="OrderAction" nillable="true" type="tns:OrderAction"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="OrderAction">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="Account" type="tns:Account"/>
<xs:element minOccurs="0" maxOccurs="1" name="ReasonType" type="xs:string"/>
<xs:element minOccurs="1" maxOccurs="1" name="ServiceReqDate" nillable="true" type="xs:dateTime"/>
<xs:element minOccurs="1" maxOccurs="1" name="OrderActionId" nillable="true" type="xs:decimal"/>
<xs:element minOccurs="0" maxOccurs="1" name="OrderActionType" type="xs:string"/>
<xs:element minOccurs="1" maxOccurs="1" name="OrderActionDate" nillable="true" type="xs:dateTime"/>
<xs:element minOccurs="1" maxOccurs="1" name="OrderActionEndDate" nillable="true" type="xs:dateTime"/>
<xs:element minOccurs="1" maxOccurs="1" name="OrderDueDate" nillable="true" type="xs:dateTime"/>
<xs:element minOccurs="0" maxOccurs="1" name="SalesChannel" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="TechName" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="SubTechName" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Comment" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Address" type="tns:Address"/>
<xs:element minOccurs="0" maxOccurs="1" name="Components" type="tns:ArrayOfComponent"/>
<xs:element minOccurs="0" maxOccurs="1" name="Attributes" type="tns:ArrayOfAttribute"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="Account">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="ExternalAccountId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="AccountNumber" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="SubscriberId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="DepartmentId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="UserTypeId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="BillTypeId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="BillModel" type="xs:string"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="ArrayOfComponent">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="unbounded" name="Component" nillable="true" type="tns:Component"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="Component">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="Id" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="ParentId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="InstanceId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="ExternalId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="ParentInstanceId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="MainInstanceId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="OfferId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="OfferInstanceId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="TechName" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="SubTechName" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Action" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Attributes" type="tns:ArrayOfAttribute"/>
<xs:element minOccurs="0" maxOccurs="1" name="PricePlans" type="tns:PricePlanList"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="ArrayOfAttribute">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="unbounded" name="Attribute" nillable="true" type="tns:Attribute"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="Attribute" mixed="true">
<xs:sequence>
<xs:any minOccurs="0" maxOccurs="1"/>
</xs:sequence>
<xs:attribute name="Name" type="xs:string"/>
<xs:attribute name="Status" type="xs:string"/>
<xs:attribute name="IsChanged" type="xs:string"/>
</xs:complexType>
<xs:complexType name="PricePlanList">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="unbounded" name="PricePlan" type="tns:PricePlan"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="PricePlan">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="CatalogId" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Action" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="Attributes" type="tns:ArrayOfAttribute"/>
</xs:sequence>
</xs:complexType>
<xs:element name="CreateOrderResponse">
<xs:complexType>
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="CreateOrderResult" type="xs:string"/>
</xs:sequence>
</xs:complexType>
</xs:element>
<xs:element name="BusinessFault" nillable="true" type="tns:BusinessFault"/>
<xs:complexType name="BusinessFault">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="Message" type="xs:string"/>
</xs:sequence>
</xs:complexType>
<xs:element name="SystemFault" nillable="true" type="tns:SystemFault"/>
<xs:complexType name="SystemFault">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="Message" type="xs:string"/>
</xs:sequence>
</xs:complexType>
<xs:element name="InputFault" nillable="true" type="tns:InputFault"/>
<xs:complexType name="InputFault">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="Message" type="xs:string"/>
<xs:element minOccurs="0" maxOccurs="1" name="ParameterName" type="xs:string"/>
</xs:sequence>
</xs:complexType>
<xs:element name="ExecuteCheckOrder">
<xs:complexType>
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="order" type="tns:Order"/>
</xs:sequence>
</xs:complexType>
</xs:element>
<xs:element name="ExecuteCheckOrderResponse">
<xs:complexType>
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="ExecuteCheckOrderResult" type="xs:string"/>
</xs:sequence>
</xs:complexType>
</xs:element>
<xs:element name="GetWfmIntervals">
<xs:complexType>
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="order" type="tns:Order"/>
<xs:element minOccurs="1" maxOccurs="1" name="dateStart" type="xs:dateTime"/>
<xs:element minOccurs="1" maxOccurs="1" name="dateEnd" type="xs:dateTime"/>
</xs:sequence>
</xs:complexType>
</xs:element>
<xs:element name="GetWfmIntervalsResponse">
<xs:complexType>
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="1" name="GetWfmIntervalsResult" type="tns:ArrayOfDateSpan"/>
</xs:sequence>
</xs:complexType>
</xs:element>
<xs:complexType name="ArrayOfDateSpan">
<xs:sequence>
<xs:element minOccurs="0" maxOccurs="unbounded" name="DateSpan" nillable="true" type="tns:DateSpan"/>
</xs:sequence>
</xs:complexType>
<xs:complexType name="DateSpan">
<xs:sequence>
<xs:element minOccurs="1" maxOccurs="1" name="DateStart" nillable="true" type="xs:dateTime"/>
<xs:element minOccurs="1" maxOccurs="1" name="DateEnd" nillable="true" type="xs:dateTime"/>
</xs:sequence>
</xs:complexType>
</xs:schema>
</wsdl:types>
<wsdl:message name="OrderService_CreateOrder_InputMessage">
<wsdl:part name="parameters" element="tns:CreateOrder"/>
</wsdl:message>
<wsdl:message name="OrderService_CreateOrder_OutputMessage">
<wsdl:part name="parameters" element="tns:CreateOrderResponse"/>
</wsdl:message>
<wsdl:message name="OrderService_CreateOrder_BusinessFaultFault_FaultMessage">
<wsdl:part name="detail" element="tns:BusinessFault"/>
</wsdl:message>
<wsdl:message name="OrderService_CreateOrder_SystemFaultFault_FaultMessage">
<wsdl:part name="detail" element="tns:SystemFault"/>
</wsdl:message>
<wsdl:message name="OrderService_CreateOrder_InputFaultFault_FaultMessage">
<wsdl:part name="detail" element="tns:InputFault"/>
</wsdl:message>
<wsdl:message name="OrderService_ExecuteCheckOrder_InputMessage">
<wsdl:part name="parameters" element="tns:ExecuteCheckOrder"/>
</wsdl:message>
<wsdl:message name="OrderService_ExecuteCheckOrder_OutputMessage">
<wsdl:part name="parameters" element="tns:ExecuteCheckOrderResponse"/>
</wsdl:message>
<wsdl:message name="OrderService_GetWfmIntervals_InputMessage">
<wsdl:part name="parameters" element="tns:GetWfmIntervals"/>
</wsdl:message>
<wsdl:message name="OrderService_GetWfmIntervals_OutputMessage">
<wsdl:part name="parameters" element="tns:GetWfmIntervalsResponse"/>
</wsdl:message>
<wsdl:message name="OrderService_GetWfmIntervals_BusinessFaultFault_FaultMessage">
<wsdl:part name="detail" element="tns:BusinessFault"/>
</wsdl:message>
<wsdl:message name="OrderService_GetWfmIntervals_SystemFaultFault_FaultMessage">
<wsdl:part name="detail" element="tns:SystemFault"/>
</wsdl:message>
<wsdl:message name="OrderService_GetWfmIntervals_InputFaultFault_FaultMessage">
<wsdl:part name="detail" element="tns:InputFault"/>
</wsdl:message>
<wsdl:portType name="OrderService">
<wsdl:operation name="CreateOrder">
<wsdl:input wsaw:Action="http://tgrad.ru/oss/OrderService/CreateOrder" message="tns:OrderService_CreateOrder_InputMessage"/>
<wsdl:output wsaw:Action="http://tgrad.ru/oss/OrderService/CreateOrderResponse" message="tns:OrderService_CreateOrder_OutputMessage"/>
<wsdl:fault wsaw:Action="http://tgrad.ru/oss/OrderService/CreateOrderBusinessFaultFault" name="BusinessFaultFault" message="tns:OrderService_CreateOrder_BusinessFaultFault_FaultMessage"/>
<wsdl:fault wsaw:Action="http://tgrad.ru/oss/OrderService/CreateOrderSystemFaultFault" name="SystemFaultFault" message="tns:OrderService_CreateOrder_SystemFaultFault_FaultMessage"/>
<wsdl:fault wsaw:Action="http://tgrad.ru/oss/OrderService/CreateOrderInputFaultFault" name="InputFaultFault" message="tns:OrderService_CreateOrder_InputFaultFault_FaultMessage"/>
</wsdl:operation>
<wsdl:operation name="ExecuteCheckOrder">
<wsdl:input wsaw:Action="http://tgrad.ru/oss/OrderService/ExecuteCheckOrder" message="tns:OrderService_ExecuteCheckOrder_InputMessage"/>
<wsdl:output wsaw:Action="http://tgrad.ru/oss/OrderService/ExecuteCheckOrderResponse" message="tns:OrderService_ExecuteCheckOrder_OutputMessage"/>
</wsdl:operation>
<wsdl:operation name="GetWfmIntervals">
<wsdl:input wsaw:Action="http://tgrad.ru/oss/OrderService/GetWfmIntervals" message="tns:OrderService_GetWfmIntervals_InputMessage"/>
<wsdl:output wsaw:Action="http://tgrad.ru/oss/OrderService/GetWfmIntervalsResponse" message="tns:OrderService_GetWfmIntervals_OutputMessage"/>
<wsdl:fault wsaw:Action="http://tgrad.ru/oss/OrderService/GetWfmIntervalsBusinessFaultFault" name="BusinessFaultFault" message="tns:OrderService_GetWfmIntervals_BusinessFaultFault_FaultMessage"/>
<wsdl:fault wsaw:Action="http://tgrad.ru/oss/OrderService/GetWfmIntervalsSystemFaultFault" name="SystemFaultFault" message="tns:OrderService_GetWfmIntervals_SystemFaultFault_FaultMessage"/>
<wsdl:fault wsaw:Action="http://tgrad.ru/oss/OrderService/GetWfmIntervalsInputFaultFault" name="InputFaultFault" message="tns:OrderService_GetWfmIntervals_InputFaultFault_FaultMessage"/>
</wsdl:operation>
</wsdl:portType>
<wsdl:binding name="BasicHttpBinding_OrderService" type="tns:OrderService">
<soap:binding transport="http://schemas.xmlsoap.org/soap/http"/>
<wsdl:operation name="CreateOrder">
<soap:operation soapAction="http://tgrad.ru/oss/OrderService/CreateOrder" style="document"/>
<wsdl:input>
<soap:body use="literal"/>
</wsdl:input>
<wsdl:output>
<soap:body use="literal"/>
</wsdl:output>
<wsdl:fault name="BusinessFaultFault">
<soap:fault use="literal" name="BusinessFaultFault" namespace=""/>
</wsdl:fault>
<wsdl:fault name="SystemFaultFault">
<soap:fault use="literal" name="SystemFaultFault" namespace=""/>
</wsdl:fault>
<wsdl:fault name="InputFaultFault">
<soap:fault use="literal" name="InputFaultFault" namespace=""/>
</wsdl:fault>
</wsdl:operation>
<wsdl:operation name="ExecuteCheckOrder">
<soap:operation soapAction="http://tgrad.ru/oss/OrderService/ExecuteCheckOrder" style="document"/>
<wsdl:input>
<soap:body use="literal"/>
</wsdl:input>
<wsdl:output>
<soap:body use="literal"/>
</wsdl:output>
</wsdl:operation>
<wsdl:operation name="GetWfmIntervals">
<soap:operation soapAction="http://tgrad.ru/oss/OrderService/GetWfmIntervals" style="document"/>
<wsdl:input>
<soap:body use="literal"/>
</wsdl:input>
<wsdl:output>
<soap:body use="literal"/>
</wsdl:output>
<wsdl:fault name="BusinessFaultFault">
<soap:fault use="literal" name="BusinessFaultFault" namespace=""/>
</wsdl:fault>
<wsdl:fault name="SystemFaultFault">
<soap:fault use="literal" name="SystemFaultFault" namespace=""/>
</wsdl:fault>
<wsdl:fault name="InputFaultFault">
<soap:fault use="literal" name="InputFaultFault" namespace=""/>
</wsdl:fault>
</wsdl:operation>
</wsdl:binding>
<wsdl:service name="OrderService">
<wsdl:port name="BasicHttpBinding_OrderService" binding="tns:BasicHttpBinding_OrderService">
<soap:address location="http://10.200.2.184/Oss/Oss.svc"/>
</wsdl:port>
</wsdl:service>
</wsdl:definitions>
```

Пример запроса:
```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
	<S:Body xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
		<oss:CreateOrder xmlns:oss="http://tgrad.ru/oss">
			<oss:order>
				<oss:SystemName>AmdocsCrm</oss:SystemName>
				<oss:OrderId>2104627</oss:OrderId>
				<oss:OrderDate>2018-06-08T17:11:28+03:00</oss:OrderDate>
				<oss:Affiliate>801</oss:Affiliate>
				<oss:RetryCount>0</oss:RetryCount>
				<oss:SalesChannel>22</oss:SalesChannel>
				<oss:TechName>xPON</oss:TechName>
				<oss:Operator>
					<oss:Login>kvelichko</oss:Login>
				</oss:Operator>
				<oss:Address>
					<oss:HouseId>692824</oss:HouseId>
					<oss:Flat>7</oss:Flat>
					<oss:DepartmentId>608993832</oss:DepartmentId>
				</oss:Address>
				<oss:Client>
					<oss:Name>ДЕМОНОВ ВИТАЛИЙ ПЕТРОВИЧ</oss:Name>
					<oss:Contact>+7 (495) 111-11-11</oss:Contact>
					<oss:IsCorp>false</oss:IsCorp>
					<oss:IsVIP>false</oss:IsVIP>
					<oss:TypeName>Не выбрано</oss:TypeName>
				</oss:Client>
				<oss:Comment/>
				<oss:OrderActions>
					<oss:OrderAction>
						<oss:Account>
							<oss:ExternalAccountId>4344391</oss:ExternalAccountId>
							<oss:AccountNumber>622010003168</oss:AccountNumber>
							<oss:SubscriberId/>
						</oss:Account>
						<oss:ServiceReqDate>2018-06-08T23:59:59+03:00</oss:ServiceReqDate>
						<oss:OrderActionId>2104628</oss:OrderActionId>
						<oss:OrderActionType>PR</oss:OrderActionType>
						<oss:OrderActionDate>2018-06-08T17:11:28+03:00</oss:OrderActionDate>
						<oss:OrderDueDate>2018-06-08T23:59:59+03:00</oss:OrderDueDate>
						<oss:Components>
							<oss:Component>
								<oss:Id>AccessLine_sib</oss:Id>
								<oss:ParentId>LineAccess_sib</oss:ParentId>
								<oss:InstanceId>162064133</oss:InstanceId>
								<oss:ExternalId/>
								<oss:ParentInstanceId>162064137</oss:ParentInstanceId>
								<oss:MainInstanceId>162064133</oss:MainInstanceId>
								<oss:OfferId>home_internet_gpon_sib</oss:OfferId>
								<oss:OfferInstanceId>162064137</oss:OfferInstanceId>
								<oss:TechName>xPON</oss:TechName>
								<oss:Action>PR</oss:Action>
								<oss:Attributes>
									<oss:Attribute Name="RecurrentSvcId" Status="AC" IsChanged="N">71998</oss:Attribute>
									<oss:Attribute Name="virtual_number" Status="AC" IsChanged="N">new</oss:Attribute>
									<oss:Attribute Name="TechName" Status="AC" IsChanged="N">xPON</oss:Attribute>
									<oss:Attribute Name="sub_technology" Status="AC" IsChanged="N">GPON</oss:Attribute>
									<oss:Attribute Name="Apply Price In All Offering" Status="AC" IsChanged="N">false</oss:Attribute>
									<oss:Attribute Name="Business role" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="InstallationAddressRequired" Status="AC" IsChanged="N">true</oss:Attribute>
									<oss:Attribute Name="Is local" Status="AC" IsChanged="N">N</oss:Attribute>
									<oss:Attribute Name="Technology" Status="AC" IsChanged="N">INDT</oss:Attribute>
									<oss:Attribute Name="Territory UBB" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="cfs_spec_id" Status="AC" IsChanged="N">line_provision</oss:Attribute>
								</oss:Attributes>
								<oss:PricePlans>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_700505</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">no</oss:Attribute>
											<oss:Attribute Name="Charge Type">OC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">700505</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,change,Change Part of Replace Offer</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">INDT</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_71998</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">no</oss:Attribute>
											<oss:Attribute Name="Charge Type">RC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">71998</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,Change Part of Replace Offer,change</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">xPON</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
								</oss:PricePlans>
							</oss:Component>
							<oss:Component>
								<oss:Id>provision_of_access_sib</oss:Id>
								<oss:ParentId>AccessLine_sib</oss:ParentId>
								<oss:InstanceId>162064146</oss:InstanceId>
								<oss:ExternalId/>
								<oss:ParentInstanceId>162064133</oss:ParentInstanceId>
								<oss:MainInstanceId>162064133</oss:MainInstanceId>
								<oss:TechName>xPON</oss:TechName>
								<oss:Action>PR</oss:Action>
								<oss:Attributes>
									<oss:Attribute Name="Apply Price In All Offering" Status="AC" IsChanged="N">false</oss:Attribute>
									<oss:Attribute Name="Business role" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="InstallationAddressRequired" Status="AC" IsChanged="N">true</oss:Attribute>
									<oss:Attribute Name="Is local" Status="AC" IsChanged="N">N</oss:Attribute>
									<oss:Attribute Name="Technology" Status="AC" IsChanged="N">INDT</oss:Attribute>
									<oss:Attribute Name="Territory UBB" Status="AC" IsChanged="N">any</oss:Attribute>
								</oss:Attributes>
								<oss:PricePlans/>
							</oss:Component>
							<oss:Component>
								<oss:Id>line_org_new_sib</oss:Id>
								<oss:ParentId>provision_of_access_sib</oss:ParentId>
								<oss:InstanceId>162064147</oss:InstanceId>
								<oss:ExternalId/>
								<oss:ParentInstanceId>162064146</oss:ParentInstanceId>
								<oss:MainInstanceId>162064133</oss:MainInstanceId>
								<oss:TechName>xPON</oss:TechName>
								<oss:Action>PR</oss:Action>
								<oss:Attributes>
									<oss:Attribute Name="Apply Price In All Offering" Status="AC" IsChanged="N">false</oss:Attribute>
									<oss:Attribute Name="Business role" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="InstallationAddressRequired" Status="AC" IsChanged="N">true</oss:Attribute>
									<oss:Attribute Name="Is local" Status="AC" IsChanged="N">N</oss:Attribute>
									<oss:Attribute Name="Technology" Status="AC" IsChanged="N">INDT</oss:Attribute>
									<oss:Attribute Name="Territory UBB" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="cfs_spec_id" Status="AC" IsChanged="N">one_time_services_provision</oss:Attribute>
								</oss:Attributes>
								<oss:PricePlans>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_97018</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">no</oss:Attribute>
											<oss:Attribute Name="Charge Type">OC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">97018</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,change,Change Part of Replace Offer</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">INDT</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
								</oss:PricePlans>
							</oss:Component>
							<oss:Component>
								<oss:Id>port_gpon_sib</oss:Id>
								<oss:ParentId>provision_of_access_sib</oss:ParentId>
								<oss:InstanceId>162064149</oss:InstanceId>
								<oss:ExternalId/>
								<oss:ParentInstanceId>162064146</oss:ParentInstanceId>
								<oss:MainInstanceId>162064133</oss:MainInstanceId>
								<oss:TechName>xPON</oss:TechName>
								<oss:Action>PR</oss:Action>
								<oss:Attributes>
									<oss:Attribute Name="RecurrentSvcId" Status="AC" IsChanged="N">97005</oss:Attribute>
									<oss:Attribute Name="Apply Price In All Offering" Status="AC" IsChanged="N">false</oss:Attribute>
									<oss:Attribute Name="Business role" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="InstallationAddressRequired" Status="AC" IsChanged="N">true</oss:Attribute>
									<oss:Attribute Name="Is local" Status="AC" IsChanged="N">N</oss:Attribute>
									<oss:Attribute Name="Technology" Status="AC" IsChanged="N">xPON</oss:Attribute>
									<oss:Attribute Name="Territory UBB" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="cfs_spec_id" Status="AC" IsChanged="N">port_provision</oss:Attribute>
								</oss:Attributes>
								<oss:PricePlans>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_97005</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">port_ubb</oss:Attribute>
											<oss:Attribute Name="Charge Type">RC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">97005</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,Change Part of Replace Offer,change</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">xPON</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_97015</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">tplan_provide</oss:Attribute>
											<oss:Attribute Name="Charge Type">OC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">97015</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,change,Change Part of Replace Offer</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">xPON</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
								</oss:PricePlans>
							</oss:Component>
							<oss:Component>
								<oss:Id>usspec_sib</oss:Id>
								<oss:ParentId>AccessLine_sib</oss:ParentId>
								<oss:InstanceId>162064152</oss:InstanceId>
								<oss:ExternalId/>
								<oss:ParentInstanceId>162064133</oss:ParentInstanceId>
								<oss:MainInstanceId>162064133</oss:MainInstanceId>
								<oss:TechName>xPON</oss:TechName>
								<oss:Action>PR</oss:Action>
								<oss:Attributes>
									<oss:Attribute Name="Apply Price In All Offering" Status="AC" IsChanged="N">false</oss:Attribute>
									<oss:Attribute Name="Business role" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="InstallationAddressRequired" Status="AC" IsChanged="N">true</oss:Attribute>
									<oss:Attribute Name="Is local" Status="AC" IsChanged="N">N</oss:Attribute>
									<oss:Attribute Name="Technology" Status="AC" IsChanged="N">INDT</oss:Attribute>
									<oss:Attribute Name="Territory UBB" Status="AC" IsChanged="N">any</oss:Attribute>
								</oss:Attributes>
								<oss:PricePlans/>
							</oss:Component>
							<oss:Component>
								<oss:Id>ustoborud_sib</oss:Id>
								<oss:ParentId>usspec_sib</oss:ParentId>
								<oss:InstanceId>162064153</oss:InstanceId>
								<oss:ExternalId/>
								<oss:ParentInstanceId>162064152</oss:ParentInstanceId>
								<oss:MainInstanceId>162064133</oss:MainInstanceId>
								<oss:TechName>xPON</oss:TechName>
								<oss:Action>PR</oss:Action>
								<oss:Attributes>
									<oss:Attribute Name="RecurrentSvcId" Status="AC" IsChanged="N">81654</oss:Attribute>
									<oss:Attribute Name="Apply Price In All Offering" Status="AC" IsChanged="N">false</oss:Attribute>
									<oss:Attribute Name="Business role" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="InstallationAddressRequired" Status="AC" IsChanged="N">true</oss:Attribute>
									<oss:Attribute Name="Is local" Status="AC" IsChanged="N">N</oss:Attribute>
									<oss:Attribute Name="Technology" Status="AC" IsChanged="N">INDT</oss:Attribute>
									<oss:Attribute Name="Territory UBB" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="cfs_spec_id" Status="AC" IsChanged="N">installation_services_provision</oss:Attribute>
								</oss:Attributes>
								<oss:PricePlans>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_81654</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">no</oss:Attribute>
											<oss:Attribute Name="Charge Type">RC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">81654</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,change,Change Part of Replace Offer</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">INDT</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_97016</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">no</oss:Attribute>
											<oss:Attribute Name="Charge Type">OC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">97016</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,change,Change Part of Replace Offer</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">INDT</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
								</oss:PricePlans>
							</oss:Component>
						</oss:Components>
					</oss:OrderAction>
					<oss:OrderAction>
						<oss:Account>
							<oss:ExternalAccountId>4344391</oss:ExternalAccountId>
							<oss:AccountNumber>622010003168</oss:AccountNumber>
							<oss:SubscriberId/>
						</oss:Account>
						<oss:ServiceReqDate>2018-06-08T23:59:59+03:00</oss:ServiceReqDate>
						<oss:OrderActionId>2104633</oss:OrderActionId>
						<oss:OrderActionType>PR</oss:OrderActionType>
						<oss:SalesChannel>2</oss:SalesChannel>
						<oss:OrderActionDate>2018-06-08T17:17:31+03:00</oss:OrderActionDate>
						<oss:OrderDueDate>2018-06-08T23:59:59+03:00</oss:OrderDueDate>
						<oss:Components>
							<oss:Component>
								<oss:Id>shpd_maincomp_sib</oss:Id>
								<oss:ParentId>Internet_access_sib</oss:ParentId>
								<oss:InstanceId>162064138</oss:InstanceId>
								<oss:ExternalId/>
								<oss:ParentInstanceId>162064137</oss:ParentInstanceId>
								<oss:MainInstanceId>162064138</oss:MainInstanceId>
								<oss:OfferId>home_internet_gpon_sib</oss:OfferId>
								<oss:OfferInstanceId>162064137</oss:OfferInstanceId>
								<oss:TechName>xPON</oss:TechName>
								<oss:Action>PR</oss:Action>
								<oss:Attributes>
									<oss:Attribute Name="RecurrentSvcId" Status="AC" IsChanged="N">97079</oss:Attribute>
									<oss:Attribute Name="TechName" Status="AC" IsChanged="N">xPON</oss:Attribute>
									<oss:Attribute Name="sub_technology" Status="AC" IsChanged="N">GPON</oss:Attribute>
									<oss:Attribute Name="night_boost_possibility" Status="AC" IsChanged="N">Y</oss:Attribute>
									<oss:Attribute Name="virtual_number" Status="AC" IsChanged="N">new</oss:Attribute>
									<oss:Attribute Name="Login" Status="AC" IsChanged="N">fttx0803-492-257</oss:Attribute>
									<oss:Attribute Name="Temp_password" Status="AC" IsChanged="N">bsWYWe16</oss:Attribute>
									<oss:Attribute Name="Apply Price In All Offering" Status="AC" IsChanged="N">false</oss:Attribute>
									<oss:Attribute Name="Business role" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="InstallationAddressRequired" Status="AC" IsChanged="N">true</oss:Attribute>
									<oss:Attribute Name="Is local" Status="AC" IsChanged="N">N</oss:Attribute>
									<oss:Attribute Name="Technology" Status="AC" IsChanged="N">INDT</oss:Attribute>
									<oss:Attribute Name="Territory UBB" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="cfs_spec_id" Status="AC" IsChanged="N">internet_access</oss:Attribute>
								</oss:Attributes>
								<oss:PricePlans>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_97017</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">tplan_provide</oss:Attribute>
											<oss:Attribute Name="Charge Type">OC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">97017</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,change,Change Part of Replace Offer</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">xPON,FTTx,xDSL</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>mra_sib_nsk_97079</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">SIB.NSK,SIB.OMS</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">no_equip2017,SIB.NSK_promo_be_with_us,SIB.NSK_be_with_us_no_tv</oss:Attribute>
											<oss:Attribute Name="Charge Type">TP</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">97079</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">replace,change,Change Part of Replace Offer,provide</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">xPON</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">SIB.NSK.4,SIB.NSK.5</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
								</oss:PricePlans>
							</oss:Component>
							<oss:Component>
								<oss:Id>sim_provision_sib</oss:Id>
								<oss:ParentId>shpd_maincomp_sib</oss:ParentId>
								<oss:InstanceId>162064142</oss:InstanceId>
								<oss:ExternalId/>
								<oss:ParentInstanceId>162064138</oss:ParentInstanceId>
								<oss:MainInstanceId>162064138</oss:MainInstanceId>
								<oss:TechName>xPON</oss:TechName>
								<oss:Action>PR</oss:Action>
								<oss:Attributes>
									<oss:Attribute Name="Apply Price In All Offering" Status="AC" IsChanged="N">false</oss:Attribute>
									<oss:Attribute Name="Business role" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="InstallationAddressRequired" Status="AC" IsChanged="N">true</oss:Attribute>
									<oss:Attribute Name="Is local" Status="AC" IsChanged="N">N</oss:Attribute>
									<oss:Attribute Name="Technology" Status="AC" IsChanged="N">INDT</oss:Attribute>
									<oss:Attribute Name="Territory UBB" Status="AC" IsChanged="N">any</oss:Attribute>
									<oss:Attribute Name="cfs_spec_id" Status="AC" IsChanged="N">sim_provision</oss:Attribute>
								</oss:Attributes>
								<oss:PricePlans>
									<oss:PricePlan>
										<oss:ExternalId/>
										<oss:CatalogId>sib_40995</oss:CatalogId>
										<oss:Action>PR</oss:Action>
										<oss:Attributes>
											<oss:Attribute Name="Affiliate">Any</oss:Attribute>
											<oss:Attribute Name="Billing system type">SIB.NSK.STR</oss:Attribute>
											<oss:Attribute Name="Business role">any</oss:Attribute>
											<oss:Attribute Name="Category for compatibility rules">no</oss:Attribute>
											<oss:Attribute Name="Charge Type">OC</oss:Attribute>
											<oss:Attribute Name="Compatibility marker">no</oss:Attribute>
											<oss:Attribute Name="Id in Billing">40995</oss:Attribute>
											<oss:Attribute Name="Is local">N</oss:Attribute>
											<oss:Attribute Name="Order Action Type">provide,replace,change,Change Part of Replace Offer</oss:Attribute>
											<oss:Attribute Name="Service Provider">RTK</oss:Attribute>
											<oss:Attribute Name="Technology">INDT</oss:Attribute>
											<oss:Attribute Name="Territory PSTN">any</oss:Attribute>
											<oss:Attribute Name="Territory UBB">any</oss:Attribute>
											<oss:Attribute Name="Price Plan Type">BS</oss:Attribute>
										</oss:Attributes>
									</oss:PricePlan>
								</oss:PricePlans>
							</oss:Component>
						</oss:Components>
					</oss:OrderAction>
				</oss:OrderActions>
				<oss:Attributes/>
			</oss:order>
		</oss:CreateOrder>
	</S:Body>
</soapenv:Envelope>
```

Как поднять сервлет на метод и реализовать маппинг XML запроса на java класс - application-example.