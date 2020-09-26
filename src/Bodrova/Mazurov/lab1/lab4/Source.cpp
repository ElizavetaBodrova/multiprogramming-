#include <iostream>
#include<vector>
#include<Windows.h>
#include<thread>
#include<atomic>
#include "SafeThreadQueue.h"

using namespace std;
#define producerCounter 2
#define customerCounter 3

SafeThreadQueue queueL;
atomic <int> customer{ 10 };
void Customer(int threadID) {
	while (customer > 0) {
		customer--;
		queueL.pop(threadID);
		
	}
}
atomic<int> producer{ 0 };

void Producer(int threadID) {

	while (producer < 10) {
		producer++;
		int value = rand() % 10;
		queueL.push(threadID,value);
		
	}
}

int main() {
	
	thread thread1[producerCounter]; 
	for (int threadInd = 0; threadInd < producerCounter; ++threadInd)
		thread1[threadInd] = thread(Producer, threadInd);

	for (int threadInd = 0; threadInd < producerCounter; ++threadInd)
		thread1[threadInd].join();

	cout << "There" << " " << endl;

	thread thread2[customerCounter];
	for (int threadInd = 0; threadInd < customerCounter; ++threadInd)
		thread2[threadInd] = thread(Customer, threadInd);

	for (int threadInd = 0; threadInd < customerCounter; ++threadInd)
		thread2[threadInd].join();
	cout << "There" << " " << endl;
	
	system("pause");
	return 0;
}
