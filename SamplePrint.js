import React from 'react';
import { Button, StyleSheet, Text, View,NativeModules } from 'react-native';
import { hsdLogo } from './dummy-logo';
const { PrinterModule } = NativeModules;
async function printreciept() {
  const columnWidths = [24, 24];
  const receiptNo = 120;
  const receiptDate = new Date();
  const originalAccount = '1239';
  const branch = 'Branch Name';
  const telephone = '123-456-7890';
  const salesman = 'John Doe';
  const productCode = 'P123';
  const amount = '500.00';
  const discount = '50.00';
  const amountReceived = '450.00';
  const paymentMethod = 'Credit Card';
  const receivedFrom = 'John Smith';
  const fcuser = 'Rukshan';
  const collectionRecieptNo = 121;

  try {
    const result = await PrinterModule?.executePrint("42817-FDPR-425-ISG");
    console.log(result);
  } catch (e) {
    alert(e.message || 'ERROR');
  }
}

const SamplePrint = () => {
  return (
    <View>
      <Text>Sample Print Instruction</Text>

      <View style={styles.btn}>
        <Button
          title="Print Receipt"
          onPress={printreciept}
        />
      </View>
    </View>
  );
};

export default SamplePrint;

const styles = StyleSheet.create({
  btn: {
    marginBottom: 8,
  },
});